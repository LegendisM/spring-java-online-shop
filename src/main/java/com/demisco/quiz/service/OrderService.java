package com.demisco.quiz.service;

import com.demisco.quiz.dto.order.AnalyzeOrdersSellDto;
import com.demisco.quiz.dto.order.OrderDto;
import com.demisco.quiz.dto.order.SubmitOrderItemRequestDto;
import com.demisco.quiz.dto.order.SubmitOrderRequestDto;
import com.demisco.quiz.entity.*;
import com.demisco.quiz.exception.ResponseException;
import com.demisco.quiz.mapper.OrderMapper;
import com.demisco.quiz.repository.OrderEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderEntityRepository orderEntityRepository;
    private final OrderMapper orderMapper;
    private final ProductService productService;
    private final UserService userService;

    /**
     * Make new order with validate order requirements and do product transactions
     * @param requestDto
     * @param userId
     * @return new maked order details
     * @throws ResponseException
     */
    @Transactional
    public OrderDto submitOrder(SubmitOrderRequestDto requestDto, Long userId) throws ResponseException {
        var user = userService.findById(userId);
        var items = this.validateSubmitOrder(requestDto.items);
        var totalPrice = this.getOrderTotalPrice(items);
        var order = this.saveOrder(
                OrderEntity.builder()
                        .status(OrderEntity.Status.PENDING)
                        .totalPrice(totalPrice)
                        .items(items)
                        .user(user)
                        .build()
        );
        this.initiateOrderProductTransactions(order);
        logger.info("New order created with id: {}", order.getId());
        return orderMapper.toDto(order);
    }

    /**
     * Validate and make order items with product versioning and quantity check
     * @param items
     * @return list of order item entities
     * @throws ResponseException
     */
    @Transactional(readOnly = true)
    public List<OrderItemEntity> validateSubmitOrder(List<SubmitOrderItemRequestDto> items) throws ResponseException {
        List<OrderItemEntity> result = new java.util.ArrayList<>(Collections.emptyList());
        var productIds = items.stream().map(SubmitOrderItemRequestDto::getProductId).toList();
        var products = productService.findByIds(productIds);
        if (products.isEmpty()) {
            throw new ResponseException("empty-product-list");
        }
        for (SubmitOrderItemRequestDto item : items) {
            var product = productService.findById(item.productId);
            productService.assertProductVersion(product, item.getProductVersion());
            if (product.getUsableBalance() < item.quantity) {
                throw new ResponseException("invalid-product-usable-quantity");
            }
            var orderItem = OrderItemEntity.builder()
                    .product(product)
                    .quantity(item.quantity)
                    .totalPrice(item.quantity * product.getPrice())
                    .build();
            result.add(orderItem);
        }
        return result;
    }

    /**
     * Take order products usable balance and add them into locked balance
     * @param order
     * @throws ResponseException
     */
    @Transactional
    public void initiateOrderProductTransactions(OrderEntity order) throws ResponseException {
        for (OrderItemEntity item : order.getItems()) {
            productService.decreaseProductUsableBalance(item.getProduct().getId(), item.getQuantity());
            productService.increaseProductLockedBalance(item.getProduct().getId(), item.getQuantity());
        }
    }

    /**
     * Take order products usable locked balance (exit products from storaae)
     * @param order
     * @throws ResponseException
     */
    @Transactional
    public void completeOrderProductTransactions(OrderEntity order) throws ResponseException {
        for (OrderItemEntity item : order.getItems()) {
            productService.decreaseProductLockedBalance(item.getProduct().getId(), item.getQuantity());
        }
    }

    /**
     * Return order products locked balance into usable balance (rollback)
     * @param order
     * @throws ResponseException
     */
    @Transactional
    public void cancelOrderProductTransactions(OrderEntity order) throws ResponseException {
        for (OrderItemEntity item : order.getItems()) {
            productService.decreaseProductLockedBalance(item.getProduct().getId(), item.getQuantity());
            productService.increaseProductUsableBalance(item.getProduct().getId(), item.getQuantity());
        }
    }

    public Double getOrderTotalPrice(List<OrderItemEntity> items) {
        Double totalPrice = 0.0;
        for (OrderItemEntity item : items) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }

    /**
     * Handle payment of order (after IPG payment callback), MVP logic
     * @param id
     * @param isSuccessfully
     * @param requestUserId
     * @return OrderDto with the new state of the order
     * @throws ResponseException
     */
    @Transactional
    public OrderDto handleOrderPayment(Long id, Boolean isSuccessfully, Long requestUserId) throws ResponseException {
        var user = userService.findById(requestUserId);
        var order = this.findById(id);
        var orderId = order.getId();
        this.validateOrderAccessForUser(order, user);
        this.assertOrderStatus(order, OrderEntity.Status.PENDING);
        if (isSuccessfully) {
            order.setStatus(OrderEntity.Status.IN_PROGRESS);
            this.saveOrder(order);
            logger.info("order payment completed with id: {}", orderId);
            logger.info("order in-progress started with id: {}", orderId);
        } else {
            logger.info("order payment failed with id: {}", orderId);
        }
        return orderMapper.toDto(order);
    }

    /**
     * Complete the order (delivered to user) and hande product exit from store
     * @param id
     * @param requestUserId
     * @return OrderDto with the new changed details
     * @throws ResponseException
     */
    @Transactional
    public OrderDto completeOrder(Long id, Long requestUserId) throws ResponseException {
        var user = userService.findById(requestUserId);
        var order = this.findById(id);
        var orderId = order.getId();
        this.validateOrderAccessForUser(order, user);
        this.assertOrderStatus(order, OrderEntity.Status.IN_PROGRESS);
        order.setStatus(OrderEntity.Status.COMPLETED);
        order.setCompletedAt(new Date());
        this.saveOrder(order);
        this.completeOrderProductTransactions(order);
        logger.info("order fully completed with id: {}", orderId);
        return orderMapper.toDto(order);
    }

    /**
     * Cancel order and return the product usable balance
     * @param id
     * @param requestUserId
     * @return OrderDto with the new changed details
     * @throws ResponseException
     */
    @Transactional
    public OrderDto cancelOrder(Long id, Long requestUserId) throws ResponseException {
        var user = userService.findById(requestUserId);
        var order = this.findById(id);
        this.validateOrderAccessForUser(order, user);
        this.assertOrderStatus(order, OrderEntity.Status.PENDING);
        order.setStatus(OrderEntity.Status.CANCELLED);
        order.setCancelledAt(new Date());
        this.saveOrder(order);
        this.cancelOrderProductTransactions(order);
        logger.info("order cancelled with id: {}", order.getId());
        return orderMapper.toDto(order);
    }

    /**
     * Calculate order sell total prices between a time
     * @param startDate
     * @param endDate
     * @return
     */
    @Transactional
    public AnalyzeOrdersSellDto analyzeOrdersSell(Date startDate, Date endDate) {
        var orders = orderEntityRepository.findAll((r, q, b) -> {
            return b.and(
                    b.equal(r.get(OrderEntity_.STATUS), OrderEntity.Status.COMPLETED),
                    b.between(r.get(OrderEntity_.COMPLETED_AT), startDate, endDate)
            );
        });
        var totalSell = 0.0;
        for (OrderEntity order : orders) {
            totalSell += order.getTotalPrice();
        }
        return AnalyzeOrdersSellDto.builder()
                .totalSell(totalSell)
                .build();
    }

    @Transactional(readOnly = true)
    public OrderEntity findById(Long id) throws ResponseException {
        return orderEntityRepository.findById(id).orElseThrow(() -> new ResponseException("order-not-found"));
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id, Long requestUserId) throws ResponseException {
        var user = userService.findById(requestUserId);
        var order = this.findById(id);
        this.validateOrderAccessForUser(order, user);
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> filterOrders(Long userId, Pageable pageable) throws ResponseException {
        var orders = orderEntityRepository.findAll((r, q, b) -> {
            return b.equal(r.join("user").get("id"), userId);
        }, pageable);
        return orders.map(orderMapper::toDto);
    }

    @Transactional
    public OrderEntity saveOrder(OrderEntity order) {
        return orderEntityRepository.save(order);
    }

    /**
     * Check the ownership of the order with the requested user
     * @param order
     * @param user
     * @throws ResponseException
     */
    @Transactional(readOnly = true)
    public void validateOrderAccessForUser(OrderEntity order, UserEntity user) throws ResponseException {
        if (!Objects.equals(order.getUser().getId(), user.getId())) {
            throw new ResponseException("invalid-user-order-access");
        }
    }

    /**
     * Check the expected order status
     * @param order
     * @param status
     * @throws ResponseException
     */
    public void assertOrderStatus(OrderEntity order, OrderEntity.Status status) throws ResponseException {
        if (order.getStatus() != status) {
            throw new ResponseException("invalid-order-status");
        }
    }


}
