package com.ingemark.demo.products.service.product;

import com.ingemark.demo.products.exception.ProductNotFoundException;
import com.ingemark.demo.products.model.dto.ProductRequestDTO;
import com.ingemark.demo.products.model.dto.ProductResponseDTO;
import com.ingemark.demo.products.model.mapper.ProductMapper;
import com.ingemark.demo.products.repository.ProductRepository;
import com.ingemark.demo.products.service.exchange.ExchangeRateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ExchangeRateService exchangeRateService;

    public ProductServiceImpl(ProductRepository productRepository, ExchangeRateService exchangeRateService) {
        this.productRepository = productRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public Mono<ProductResponseDTO> createProduct(final ProductRequestDTO requestDTO) {
        return Mono.fromCallable(() -> ProductMapper.toEntity(requestDTO))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(product -> Mono.fromCallable(() -> productRepository.save(product))
                        .subscribeOn(Schedulers.boundedElastic())
                )
                .flatMap(savedProduct -> exchangeRateService.getExchangeRate("USD")
                        .map(exchangeRate -> ProductMapper.toResponseDTO(savedProduct)
                                .withCalculatedPriceInUsd(exchangeRate))
                );
    }

    @Override
    public Mono<ProductResponseDTO> getProductById(final UUID id) {
        return Mono.fromCallable(() -> productRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalProduct -> optionalProduct.map(product -> exchangeRateService.getExchangeRate("USD")
                        .map(exchangeRate -> ProductMapper.toResponseDTO(product)
                                .withCalculatedPriceInUsd(exchangeRate)))
                        .orElseGet(() -> Mono.error(new ProductNotFoundException("Product not found with ID: " + id))));
    }

    @Override
    public Mono<Page<ProductResponseDTO>> getAllProducts(final Pageable pageable) {
        return exchangeRateService.getExchangeRate("USD")
                .flatMap(exchangeRate -> Mono.fromCallable(() -> productRepository.findAll(pageable))
                        .subscribeOn(Schedulers.boundedElastic())
                        .map(page -> page.map(product -> ProductMapper.toResponseDTO(product)
                                .withCalculatedPriceInUsd(exchangeRate))
                        )
                );
    }
}
