package com.ingemark.demo.products.repository.cache;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateCacheRepository {
    void saveExchangeRate(String key, BigDecimal exchangeRate);
    Optional<BigDecimal> getExchangeRate(String key);
}
