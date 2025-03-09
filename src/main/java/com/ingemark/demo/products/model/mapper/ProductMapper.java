package com.ingemark.demo.products.model.mapper;

import com.ingemark.demo.products.model.dto.ProductRequestDto;
import com.ingemark.demo.products.model.dto.ProductResponseDto;
import com.ingemark.demo.products.model.entity.Product;

import java.math.BigDecimal;

public class ProductMapper {

    private ProductMapper() {
    }

    public static Product toEntity(final ProductRequestDto requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setCode(requestDTO.code());
        product.setName(requestDTO.name());
        product.setPriceEur(requestDTO.priceEur());
        product.setAvailable(requestDTO.isAvailable());

        return product;
    }

    public static ProductResponseDto toResponseDto(
            final Product product,
            final BigDecimal exchangeRate
    ) {
        if (product == null) {
            return null;
        }

        return new ProductResponseDto(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getPriceEur(),
                product.getPriceEur().multiply(exchangeRate),
                product.isAvailable()
        );
    }
}
