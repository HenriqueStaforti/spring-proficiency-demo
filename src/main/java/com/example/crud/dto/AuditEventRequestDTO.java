package com.example.crud.dto;

import com.example.crud.enums.AuditAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record AuditEventRequestDTO(
        @NotBlank(message = "Entity type is required")
        String entityType,

        @NotBlank(message = "Entity ID is required")
        String entityId,

        @NotNull(message = "Action is required")
        AuditAction action,

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Event source is required")
        String source,

        @NotNull(message = "Timestamp is required")
        @PastOrPresent(message = "Timestamp cannot be in the future")
        Instant timestamp,

        String description,

        Map<String, Object> metadata
) {}
