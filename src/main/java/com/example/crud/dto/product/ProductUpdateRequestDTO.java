package com.example.crud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductUpdateRequestDTO(
        String name,

        @PositiveOrZero
        Double price,

        Boolean enabled) {
}
