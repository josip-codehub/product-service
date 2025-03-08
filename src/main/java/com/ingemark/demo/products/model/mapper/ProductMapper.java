package com.ingemark.demo.products.model.mapper;

import com.ingemark.demo.products.model.dto.ProductRequestDTO;
import com.ingemark.demo.products.model.dto.ProductResponseDTO;
import com.ingemark.demo.products.model.entity.Product;

import java.math.BigDecimal;

public class ProductMapper {

    public static Product toEntity(ProductRequestDTO requestDTO) {
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

    public static ProductResponseDTO toResponseDTO(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductResponseDTO(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getPriceEur(),
                product.getPriceEur().multiply(BigDecimal.valueOf(1.1)),
                product.isAvailable()
        );
    }
}
