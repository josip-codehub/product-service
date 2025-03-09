package com.ingemark.demo.products.repository;

import com.ingemark.demo.products.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByCode(final String code);
}
