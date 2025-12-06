package com.example.crud.dto.product;

import jakarta.validation.constraints.PositiveOrZero;

public record ProductUpdateRequestDTO(
        String name,

        @PositiveOrZero
        Double price,

        Boolean enabled) {
}
