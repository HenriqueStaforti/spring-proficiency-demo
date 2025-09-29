package com.example.crud.dto;

import java.time.Instant;

public record TokenDTO(String token, Instant expiresAt) {}
