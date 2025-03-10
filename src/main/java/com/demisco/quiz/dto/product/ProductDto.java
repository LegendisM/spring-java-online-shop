package com.demisco.quiz.dto.product;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {

    private String title;
    private String description;
    private Double price;
    private Integer usableBalance;
    private Integer lockedBalance;

}
