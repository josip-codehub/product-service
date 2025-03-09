package com.ingemark.demo.products.model.entity;

import jakarta.persistence.*;

import java.util.UUID;

@MappedSuperclass
public abstract class Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    protected Identifiable() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
