package com.demisco.quiz.dto.order;

import com.demisco.quiz.entity.OrderEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AnalyzeOrdersSellDto {

    private Double totalSell;

}
