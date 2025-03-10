package com.ingemark.demo.products.service.product;

import com.ingemark.demo.products.exception.ProductNotFoundException;
import com.ingemark.demo.products.model.dto.ProductRequestDto;
import com.ingemark.demo.products.model.dto.ProductResponseDto;
import com.ingemark.demo.products.model.entity.Product;
import com.ingemark.demo.products.model.mapper.ProductMapper;
import com.ingemark.demo.products.repository.ProductRepository;
import com.ingemark.demo.products.service.exchange.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequestDto requestDto;
    private Product product;
    private BigDecimal exchangeRate;

    @BeforeEach
    void setUp() {
        requestDto = new ProductRequestDto("1234567890", "Test Product", BigDecimal.valueOf(100), true);
        product = ProductMapper.toEntity(requestDto);
        product.setId(UUID.randomUUID());
        exchangeRate = BigDecimal.valueOf(1.1);
    }

    @Test
    void createProduct_Success() {
        when(productRepository.save(any())).thenReturn(product);
        when(exchangeRateService.getExchangeRate("USD")).thenReturn(Mono.just(exchangeRate));

        Mono<ProductResponseDto> result = productService.createProduct(requestDto);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.code().equals(requestDto.code())
                        && response.name().equals(requestDto.name())
                        && response.priceEur().compareTo(requestDto.priceEur()) == 0
                        && response.priceUsd().compareTo(requestDto.priceEur().multiply(exchangeRate)) == 0
                        && response.isAvailable() == requestDto.isAvailable())
                .verifyComplete();
    }

    @Test
    void createProduct_WhenExchangeRateFails_ShouldThrowException() {
        when(productRepository.save(any())).thenReturn(product);
        when(exchangeRateService.getExchangeRate("USD"))
                .thenReturn(Mono.error(new RuntimeException("Exchange rate API failure")));

        Mono<ProductResponseDto> result = productService.createProduct(requestDto);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Exchange rate API failure"))
                .verify();
    }

    @Test
    void createProduct_WhenSavingFails_ShouldThrowException() {
        when(productRepository.save(any()))
                .thenThrow(new RuntimeException("Database error"));

        Mono<ProductResponseDto> result = productService.createProduct(requestDto);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Database error"))
                .verify();
    }

    @Test
    void getProductById_Success() {
        UUID productId = product.getId();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(exchangeRateService.getExchangeRate("USD")).thenReturn(Mono.just(exchangeRate));

        Mono<ProductResponseDto> result = productService.getProductById(productId);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.id().equals(productId)
                        && response.code().equals(product.getCode())
                        && response.name().equals(product.getName())
                        && response.priceEur().compareTo(product.getPriceEur()) == 0
                        && response.priceUsd().compareTo(product.getPriceEur().multiply(exchangeRate)) == 0
                        && response.isAvailable() == product.isAvailable())
                .verifyComplete();
    }

    @Test
    void getProductById_WhenProductNotFound_ShouldThrowException() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Mono<ProductResponseDto> result = productService.getProductById(productId);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException &&
                        throwable.getMessage().equals("Product not found with ID: " + productId))
                .verify();
    }

    @Test
    void getAllProducts_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = List.of(product);
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        when(exchangeRateService.getExchangeRate("USD")).thenReturn(Mono.just(exchangeRate));
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Mono<Page<ProductResponseDto>> result = productService.getAllProducts(pageable);

        StepVerifier.create(result)
                .expectNextMatches(page -> !page.isEmpty()
                        && page.getContent().get(0).id().equals(product.getId())
                        && page.getContent().get(0).priceUsd().compareTo(product.getPriceEur().multiply(exchangeRate)) == 0)
                .verifyComplete();
    }

    @Test
    void getAllProducts_WhenExchangeRateFails_ShouldThrowException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(exchangeRateService.getExchangeRate("USD")).thenReturn(Mono.error(new RuntimeException("Exchange rate error")));

        Mono<Page<ProductResponseDto>> result = productService.getAllProducts(pageable);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Exchange rate error"))
                .verify();
    }
}
