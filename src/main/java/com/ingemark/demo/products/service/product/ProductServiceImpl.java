package com.ingemark.demo.products.service.product;

import com.ingemark.demo.products.exception.ProductNotFoundException;
import com.ingemark.demo.products.model.dto.ProductRequestDto;
import com.ingemark.demo.products.model.dto.ProductResponseDto;
import com.ingemark.demo.products.model.mapper.ProductMapper;
import com.ingemark.demo.products.repository.ProductRepository;
import com.ingemark.demo.products.service.exchange.ExchangeRateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ExchangeRateService exchangeRateService;

    public ProductServiceImpl(
            final ProductRepository productRepository,
            final ExchangeRateService exchangeRateService
    ) {
        this.productRepository = productRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public Mono<ProductResponseDto> createProduct(final ProductRequestDto requestDTO) {
        return Mono.fromCallable(() -> ProductMapper.toEntity(requestDTO))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(product -> Mono.fromCallable(() -> productRepository.save(product))
                        .subscribeOn(Schedulers.boundedElastic())
                )
                .flatMap(savedProduct -> exchangeRateService.getExchangeRate("USD")
                        .mapNotNull(exchangeRate -> ProductMapper.toResponseDto(savedProduct, exchangeRate))
                );
    }

    @Override
    public Mono<ProductResponseDto> getProductById(final UUID id) {
        return Mono.fromCallable(() -> productRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalProduct -> optionalProduct.map(product -> exchangeRateService.getExchangeRate("USD")
                        .map(exchangeRate -> ProductMapper.toResponseDto(product, exchangeRate)))
                        .orElseGet(() -> Mono.error(new ProductNotFoundException("Product not found with ID: " + id))));
    }

    @Override
    public Mono<Page<ProductResponseDto>> getAllProducts(final Pageable pageable) {
        return exchangeRateService.getExchangeRate("USD")
                .flatMap(exchangeRate -> Mono.fromCallable(() -> productRepository.findAll(pageable))
                        .subscribeOn(Schedulers.boundedElastic())
                        .map(page -> page.map(product -> ProductMapper.toResponseDto(product, exchangeRate))
                        )
                );
    }
}
