package com.ingemark.demo.products.service.product;

import com.ingemark.demo.products.model.dto.ProductRequestDTO;
import com.ingemark.demo.products.model.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductService {
    Mono<ProductResponseDTO> createProduct(ProductRequestDTO requestDTO);
    Mono<ProductResponseDTO> getProductById(UUID id);
    Mono<Page<ProductResponseDTO>> getAllProducts(Pageable pageable);
}
