package com.demisco.quiz.controller;

import com.demisco.quiz.annotation.Auth;
import com.demisco.quiz.annotation.CurrentUser;
import com.demisco.quiz.dto.ApiResponse;
import com.demisco.quiz.dto.product.AddProductRequestDto;
import com.demisco.quiz.dto.product.ProductDto;
import com.demisco.quiz.dto.product.UpdateProductRequestDto;
import com.demisco.quiz.dto.user.UserDto;
import com.demisco.quiz.entity.UserEntity;
import com.demisco.quiz.exception.ResponseException;
import com.demisco.quiz.service.ProductService;
import com.demisco.quiz.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/filter-products")
    @Auth
    public ApiResponse<Page<ProductDto>> filterProducts(@PageableDefault Pageable pageable) throws ResponseException {
        var products = productService.filterProducts(pageable);
        return new ApiResponse<>(true, "successfully", products);
    }

    @PostMapping("/create-product")
    @Auth
    public ApiResponse<ProductDto> createProduct(@RequestBody @Valid AddProductRequestDto requestDto) throws ResponseException {
        var product = productService.createProduct(requestDto);
        return new ApiResponse<>(true, "successfully", product);
    }

    @PatchMapping("/update-product/{id}")
    @Auth
    public ApiResponse<ProductDto> updateProduct(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateProductRequestDto requestDto
    ) throws ResponseException {
        var product = productService.updateProduct(id, requestDto);
        return new ApiResponse<>(true, "successfully", product);
    }

    @DeleteMapping("/delete-product/{id}")
    @Auth
    public ApiResponse<Boolean> updateProduct(@PathVariable("id") Long id) throws ResponseException {
        productService.deleteProduct(id);
        return new ApiResponse<>(true, "successfully", true);
    }

}
