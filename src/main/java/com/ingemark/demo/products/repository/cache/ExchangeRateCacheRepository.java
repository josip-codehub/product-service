package com.ingemark.demo.products.repository.cache;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateCacheRepository {
    void saveExchangeRate(
            final String key,
            final BigDecimal exchangeRate
    );
    Optional<BigDecimal> getExchangeRate(final String key);
}
