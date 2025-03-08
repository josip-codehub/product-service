package com.ingemark.demo.products.controller;

import com.ingemark.demo.products.model.dto.PageResponse;
import com.ingemark.demo.products.model.dto.ProductRequestDTO;
import com.ingemark.demo.products.model.dto.ProductResponseDTO;
import com.ingemark.demo.products.service.product.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @PostMapping
    public Mono<ResponseEntity<ProductResponseDTO>> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        return productService.createProduct(requestDTO)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Mono<ResponseEntity<PageResponse<ProductResponseDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProducts(pageable)
                .map(response -> ResponseEntity.ok(new PageResponse<>(response)));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductResponseDTO>> getProductById(
            @Parameter(description = "Product ID", required = true, schema = @Schema(type = "string", format = "uuid"))
            @PathVariable UUID id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
