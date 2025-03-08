package com.ingemark.demo.products.service.exchange;

import com.ingemark.demo.products.exception.ExchangeRateException;
import com.ingemark.demo.products.model.dto.ExchangeRateResponse;
import com.ingemark.demo.products.repository.cache.ExchangeRateCacheRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Value("${hnb.api.url}")
    private String hnbApiUrl;
    private final WebClient.Builder webClientBuilder;
    private final ExchangeRateCacheRepository exchangeRateCacheRepository;

    public ExchangeRateServiceImpl(WebClient.Builder webClientBuilder, ExchangeRateCacheRepository exchangeRateCacheRepository) {
        this.webClientBuilder = webClientBuilder;
        this.exchangeRateCacheRepository = exchangeRateCacheRepository;
    }

    @Override
    public Mono<BigDecimal> getExchangeRate(final String currency) {
        return Mono.justOrEmpty(exchangeRateCacheRepository.getExchangeRate(currency))
                .switchIfEmpty(fetchExchangeRateFromApi(currency)
                        .doOnSuccess(rate -> exchangeRateCacheRepository.saveExchangeRate(currency, rate)));
    }

    private Mono<BigDecimal> fetchExchangeRateFromApi(String currency) {
        String url = hnbApiUrl + "?valuta=" + currency;

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(ExchangeRateResponse[].class)
                .handle((exchangeRates, sink) -> {
                    if (exchangeRates != null && exchangeRates.length > 0) {
                        sink.next(exchangeRates[0].middleRate());
                    } else {
                        sink.error(new ExchangeRateException("Exchange rate not found for currency: " + currency));
                    }
                });
    }
}
