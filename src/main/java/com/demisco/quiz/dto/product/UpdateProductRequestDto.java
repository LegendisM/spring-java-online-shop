package com.demisco.quiz.dto.product;

import lombok.Data;

@Data
public class UpdateProductRequestDto {

    public String title;
    public String description;
    public Double price;
    public Integer usableBalance;
    public Integer lockedBalance;

}
