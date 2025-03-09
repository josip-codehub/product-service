package com.ingemark.demo.products.model.dto;

import com.ingemark.demo.products.validation.UniqueCode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequestDto(
        @Size(min = 10, max = 10, message = "Code must be exactly 10 characters long")
        @UniqueCode
        String code,

        @NotBlank(message = "Name is required")
        String name,

        @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
        BigDecimal priceEur,

        boolean isAvailable
) {}
