package com.demisco.quiz.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class SubmitOrderRequestDto {

    public List<SubmitOrderItemRequestDto> items;

}