package com.ingemark.demo.products.exception;

import org.springframework.http.HttpStatus;

public class ExchangeRateException extends BaseException {
    public ExchangeRateException(final String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
