package com.demisco.quiz.mapper;

import com.demisco.quiz.dto.product.ProductDto;
import com.demisco.quiz.entity.ProductEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public ProductDto toDto(ProductEntity product) {
        return ProductDto.builder()
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .usableBalance(product.getUsableBalance())
                .lockedBalance(product.getLockedBalance())
                .build();
    }


}
