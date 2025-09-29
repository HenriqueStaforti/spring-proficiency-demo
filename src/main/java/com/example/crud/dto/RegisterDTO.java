package com.example.crud.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(
        @NotBlank String username,
        @NotBlank String password
) {}
