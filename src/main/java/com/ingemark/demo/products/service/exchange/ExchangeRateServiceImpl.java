package com.ingemark.demo.products.service.exchange;

import com.ingemark.demo.products.integration.hnb.HnbApiClient;
import com.ingemark.demo.products.repository.cache.ExchangeRateCacheRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateCacheRepository exchangeRateCacheRepository;
    private final HnbApiClient hnbApiClient;

    public ExchangeRateServiceImpl(
            ExchangeRateCacheRepository exchangeRateCacheRepository,
            HnbApiClient hnbApiClient
    ) {
        this.exchangeRateCacheRepository = exchangeRateCacheRepository;
        this.hnbApiClient = hnbApiClient;
    }

    @Override
    public Mono<BigDecimal> getExchangeRate(final String currency) {
        return Mono
                .justOrEmpty(exchangeRateCacheRepository.getExchangeRate(currency))
                .switchIfEmpty(
                        hnbApiClient.fetchExchangeRateFromApi(currency)
                        .doOnSuccess(rate -> exchangeRateCacheRepository.saveExchangeRate(currency, rate))
                );
    }


}
