package com.ingemark.demo.products.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueCodeValidator.class)
public @interface UniqueCode {
    String message() default "Product code already exists.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
