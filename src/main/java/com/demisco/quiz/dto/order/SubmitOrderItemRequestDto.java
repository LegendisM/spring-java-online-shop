package com.demisco.quiz.dto.order;

import lombok.Data;

@Data
public class SubmitOrderItemRequestDto {


    public Long productId;
    private Integer productVersion;
    public Integer quantity;

}
