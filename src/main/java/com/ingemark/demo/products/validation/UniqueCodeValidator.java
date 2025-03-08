package com.ingemark.demo.products.validation;

import com.ingemark.demo.products.repository.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueCodeValidator implements ConstraintValidator<UniqueCode, String> {

    private final ProductRepository productRepository;

    public UniqueCodeValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        return code != null && !productRepository.existsByCode(code);
    }
}
