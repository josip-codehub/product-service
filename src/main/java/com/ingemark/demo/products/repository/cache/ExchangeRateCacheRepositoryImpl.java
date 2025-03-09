package com.ingemark.demo.products.repository.cache;

import com.ingemark.demo.products.util.TimeUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class ExchangeRateCacheRepositoryImpl implements ExchangeRateCacheRepository {

    private final RedisTemplate<String, BigDecimal> redisTemplate;

    public ExchangeRateCacheRepositoryImpl(final RedisTemplate<String, BigDecimal> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveExchangeRate(
            final String key,
            final BigDecimal exchangeRate
    ) {
        final long ttl = TimeUtil.calculateTimeToMidnight();
        redisTemplate
                .opsForValue()
                .set(key, exchangeRate, ttl, TimeUnit.SECONDS);
    }

    @Override
    public Optional<BigDecimal> getExchangeRate(final String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }
}
