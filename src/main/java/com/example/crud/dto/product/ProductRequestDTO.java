package com.example.crud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductRequestDTO(
        @NotBlank
        String name,

        @NotNull
        @PositiveOrZero
        Double price,

        @NotNull
        Boolean enabled) {
}
