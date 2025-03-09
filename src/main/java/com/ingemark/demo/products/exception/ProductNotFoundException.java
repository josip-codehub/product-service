package com.ingemark.demo.products.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends BaseException {
    public ProductNotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
