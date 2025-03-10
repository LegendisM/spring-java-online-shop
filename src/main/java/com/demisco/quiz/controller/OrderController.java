package com.demisco.quiz.controller;

import com.demisco.quiz.annotation.Auth;
import com.demisco.quiz.annotation.CurrentUser;
import com.demisco.quiz.dto.ApiResponse;
import com.demisco.quiz.dto.order.AnalyzeOrdersSellDto;
import com.demisco.quiz.dto.order.OrderDto;
import com.demisco.quiz.dto.order.SubmitOrderRequestDto;
import com.demisco.quiz.dto.product.AddProductRequestDto;
import com.demisco.quiz.dto.product.ProductDto;
import com.demisco.quiz.dto.product.UpdateProductRequestDto;
import com.demisco.quiz.entity.UserEntity;
import com.demisco.quiz.exception.ResponseException;
import com.demisco.quiz.service.OrderService;
import com.demisco.quiz.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/submit-order")
    @Auth
    public ApiResponse<OrderDto> createOrder(
            @RequestBody @Valid SubmitOrderRequestDto requestDto,
            @CurrentUser UserEntity user
    ) throws ResponseException {
        var order = orderService.submitOrder(requestDto, user.getId());
        return new ApiResponse<>(true, "successfully", order);
    }

    @PostMapping("/filter-orders")
    @Auth
    public ApiResponse<Page<OrderDto>> filterOrders(
            @CurrentUser UserEntity user,
            @PageableDefault Pageable pageable
    ) throws ResponseException {
        var orders = orderService.filterOrders(user.getId(), pageable);
        return new ApiResponse<>(true, "successfully", orders);
    }

    @GetMapping("/order-detail/{id}")
    @Auth
    public ApiResponse<OrderDto> getOrderDetails(
            @PathVariable("id") Long id,
            @CurrentUser UserEntity user
    ) throws ResponseException {
        var order = orderService.getOrderById(id, user.getId());
        return new ApiResponse<>(true, "successfully", order);
    }

    @PostMapping("/handle-order-payment/{id}")
    @Auth
    public ApiResponse<OrderDto> handleOrderPayment(
            @PathVariable("id") Long id,
            @RequestParam("isSuccessfully") Boolean isSuccessfully,
            @CurrentUser UserEntity user
    ) throws ResponseException {
        var order = orderService.handleOrderPayment(id, isSuccessfully, user.getId());
        return new ApiResponse<>(true, "successfully", order);
    }

    @PostMapping("/complete-order/{id}")
    @Auth
    public ApiResponse<OrderDto> completeOrder(
            @PathVariable("id") Long id,
            @CurrentUser UserEntity user
    ) throws ResponseException {
        var order = orderService.completeOrder(id, user.getId());
        return new ApiResponse<>(true, "successfully", order);
    }

    @PostMapping("/cancel-order/{id}")
    @Auth
    public ApiResponse<OrderDto> handleOrderPayment(
            @PathVariable("id") Long id,
            @CurrentUser UserEntity user
    ) throws ResponseException {
        var order = orderService.cancelOrder(id, user.getId());
        return new ApiResponse<>(true, "successfully", order);
    }

    @PostMapping("/analyze-orders-sell")
    @Auth
    public ApiResponse<AnalyzeOrdersSellDto> analyzeOrdersSell(
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate
    ) throws ResponseException {
        var analyzeOrdersSellDto = orderService.analyzeOrdersSell(startDate, endDate);
        return new ApiResponse<>(true, "successfully", analyzeOrdersSellDto);
    }

}
