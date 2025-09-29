package com.example.crud.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ApiErrorDTO(Instant timestamp, int status, String error, String message, String path) {

    public ApiErrorDTO(HttpStatus status, String message, String path) {
        this(Instant.now(), status.value(), status.getReasonPhrase(), message, path);
    }
}
