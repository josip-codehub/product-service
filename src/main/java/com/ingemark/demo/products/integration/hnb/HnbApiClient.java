package com.ingemark.demo.products.integration.hnb;

import com.ingemark.demo.products.exception.ExchangeRateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
public class HnbApiClient {
    @Value("${hnb.api.url}")
    private String hnbApiUrl;

    private final WebClient webClient;

    public HnbApiClient() {
        webClient = WebClient.builder().build();
    }

    public Mono<BigDecimal> fetchExchangeRateFromApi(String currency) {
        String url = hnbApiUrl + "?valuta=" + currency;

        return webClient
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
