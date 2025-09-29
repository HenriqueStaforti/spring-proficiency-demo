package com.example.crud.dto;

import com.example.crud.model.ProductEntity;

import java.time.Instant;

public record ProductResponseDTO(Long id, String name, Double price, Boolean enabled, Instant createdAt) {

    public ProductResponseDTO(ProductEntity product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getEnabled(), product.getCreatedAt());
    }
}
