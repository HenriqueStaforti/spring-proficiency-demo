package com.example.crud.dto.user;

import java.time.Instant;

public record TokenDTO(String token, Instant expiresAt) {}
