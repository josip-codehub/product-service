package com.ingemark.demo.products.service.exchange;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ExchangeRateService {
    Mono<BigDecimal> getExchangeRate(final String currency);
}
