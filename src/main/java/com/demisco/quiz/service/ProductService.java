package com.demisco.quiz.service;

import com.demisco.quiz.dto.product.AddProductRequestDto;
import com.demisco.quiz.dto.product.ProductDto;
import com.demisco.quiz.dto.product.UpdateProductRequestDto;
import com.demisco.quiz.entity.ProductEntity;
import com.demisco.quiz.exception.ResponseException;
import com.demisco.quiz.mapper.ProductMapper;
import com.demisco.quiz.repository.ProductEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductEntityRepository productEntityRepository;
    private final ProductMapper productMapper;

    /**
     * Make new product by the admin
     * @param requestDto
     * @return ProductDto details
     */
    @Transactional
    public ProductDto createProduct(AddProductRequestDto requestDto) {
        var product = this.saveProduct(
                ProductEntity.builder()
                        .title(requestDto.title)
                        .description(requestDto.description)
                        .price(requestDto.price)
                        .usableBalance(requestDto.usableBalance)
                        .lockedBalance(requestDto.lockedBalance)
                        .build()
        );
        logger.info("New product created with id: {}", product.getId());
        return productMapper.toDto(product);
    }

    /**
     * Update the product details by the admin
     * @param id
     * @param requestDto
     * @return ProductDto wtih the new details
     * @throws ResponseException
     */
    @Transactional
    public ProductDto updateProduct(Long id, UpdateProductRequestDto requestDto) throws ResponseException {
        var product = this.findById(id);
        product.setTitle(requestDto.title);
        product.setDescription(requestDto.description);
        product.setPrice(requestDto.price);
        product.setUsableBalance(requestDto.usableBalance);
        product.setLockedBalance(requestDto.lockedBalance);
        this.saveProduct(product);
        logger.info("product updated with id: {}", product.getId());
        return productMapper.toDto(product);
    }

    /**
     * Delete the product by the admin with id
     * @param id
     * @throws ResponseException
     */
    @Transactional
    public void deleteProduct(Long id) throws ResponseException {
        var product = this.findById(id);
        logger.info("product deleted with id: {}", product.getId());
        productEntityRepository.delete(product);
    }

    /**
     * Increase the product usable balance
     * @param id
     * @param count
     * @return
     * @throws ResponseException
     */
    @Transactional
    public ProductEntity increaseProductUsableBalance(Long id, Integer count) throws ResponseException {
        var product = this.findById(id);
        product.setUsableBalance(product.getUsableBalance() + count);
        this.saveProduct(product);
        logger.info("product usable balance updated with id: {}", product.getId());
        return product;
    }

    /**
     * Decrease product usable balance
     * @param id
     * @param count
     * @return
     * @throws ResponseException
     */
    @Transactional
    public ProductEntity decreaseProductUsableBalance(Long id, Integer count) throws ResponseException {
        var product = this.findById(id);
        product.setUsableBalance(product.getUsableBalance() - count);
        this.saveProduct(product);
        logger.info("product usable balance updated with id: {}", product.getId());
        return product;
    }

    /**
     * Increase product locked balance
     * @param id
     * @param count
     * @return
     * @throws ResponseException
     */
    @Transactional
    public ProductEntity increaseProductLockedBalance(Long id, Integer count) throws ResponseException {
        var product = this.findById(id);
        product.setLockedBalance(product.getLockedBalance() + count);
        this.saveProduct(product);
        logger.info("product usable balance updated with id: {}", product.getId());
        return product;
    }

    /**
     * Decrease product locked balance
     * @param id
     * @param count
     * @return
     * @throws ResponseException
     */
    @Transactional
    public ProductEntity decreaseProductLockedBalance(Long id, Integer count) throws ResponseException {
        var product = this.findById(id);
        product.setLockedBalance(product.getLockedBalance() - count);
        this.saveProduct(product);
        logger.info("product usable balance updated with id: {}", product.getId());
        return product;
    }


    @Transactional(readOnly = true)
    public ProductEntity findById(Long id) throws ResponseException {
        return productEntityRepository.findById(id).orElseThrow(() -> new ResponseException("product-not-found"));
    }

    @Transactional(readOnly = true)
    public List<ProductEntity> findByIds(List<Long> ids) {
        return productEntityRepository.findAllById(ids);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) throws ResponseException {
        var product = this.findById(id);
        return productMapper.toDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> filterProducts(Pageable pageable) {
        var products = productEntityRepository.findAll(pageable);
        return products.map(productMapper::toDto);
    }

    @Transactional
    public ProductEntity saveProduct(ProductEntity product) {
        return productEntityRepository.save(product);
    }

    /**
     * Assert product version to prevent concurrency
     * @param product
     * @param version
     * @throws ResponseException
     */
    public void assertProductVersion(ProductEntity product, Integer version) throws ResponseException {
        if (!Objects.equals(product.getVersion(), version)) {
            throw new ResponseException("invalid-product-version");
        }
    }

}
