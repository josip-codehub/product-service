package com.ingemark.demo.products.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String code,
        String name,
        BigDecimal priceEur,
        BigDecimal priceUsd,
        boolean isAvailable
) {
    public ProductResponseDTO withCalculatedPriceInUsd(final BigDecimal exchangeRate) {
        BigDecimal calculatedUsdPrice = this.priceEur.multiply(exchangeRate);
        return new ProductResponseDTO(id, code, name, priceEur, calculatedUsdPrice, isAvailable);
    }
}
