package com.demisco.quiz.mapper;

import com.demisco.quiz.dto.order.OrderDto;
import com.demisco.quiz.dto.order.OrderItemDto;
import com.demisco.quiz.dto.product.ProductDto;
import com.demisco.quiz.entity.OrderEntity;
import com.demisco.quiz.entity.OrderItemEntity;
import com.demisco.quiz.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMapper {

    private final ProductMapper productMapper;

    public OrderDto toDto(OrderEntity order) {
        return OrderDto.builder()
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .items(order.getItems().stream().map(this::toDto).toList())
                .build();
    }

    public OrderItemDto toDto(OrderItemEntity orderItem) {
        return OrderItemDto.builder()
                .product(productMapper.toDto(orderItem.getProduct()))
                .quantity(orderItem.getQuantity())
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }


}
