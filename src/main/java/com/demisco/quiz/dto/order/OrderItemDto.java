package com.demisco.quiz.dto.order;

import com.demisco.quiz.dto.product.ProductDto;
import com.demisco.quiz.entity.OrderEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderItemDto {

    private ProductDto product;
    private Integer quantity;
    private Double totalPrice;

}
