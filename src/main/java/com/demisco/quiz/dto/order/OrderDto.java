package com.demisco.quiz.dto.order;

import com.demisco.quiz.dto.product.ProductDto;
import com.demisco.quiz.entity.OrderEntity;
import com.demisco.quiz.entity.OrderItemEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDto {

    private List<OrderItemDto> items;
    private OrderEntity.Status status;
    private Double totalPrice;

}
