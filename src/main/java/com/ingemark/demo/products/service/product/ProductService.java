package com.ingemark.demo.products.service.product;

import com.ingemark.demo.products.model.dto.ProductRequestDto;
import com.ingemark.demo.products.model.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductService {
    Mono<ProductResponseDto> createProduct(ProductRequestDto requestDTO);
    Mono<ProductResponseDto> getProductById(UUID id);
    Mono<Page<ProductResponseDto>> getAllProducts(Pageable pageable);
}
