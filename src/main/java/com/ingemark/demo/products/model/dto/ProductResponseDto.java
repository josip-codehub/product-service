package com.ingemark.demo.products.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String code,
        String name,
        BigDecimal priceEur,
        BigDecimal priceUsd,
        boolean isAvailable
) { }
