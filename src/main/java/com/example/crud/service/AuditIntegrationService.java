package com.example.crud.service;

import com.example.crud.client.AuditClient;
import com.example.crud.dto.AuditEventRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuditIntegrationService {
    private final AuditClient auditClient;

    public AuditIntegrationService(AuditClient auditClient) {
        this.auditClient = auditClient;
    }

    public void sendAuditEvent(AuditEventRequestDTO auditEventRequestDTO) {
        try {
            auditClient.sendAuditEvent(auditEventRequestDTO);
            log.info("Audit event sent successfully");
        } catch (Exception e) {
            log.error("Failed to send audit event", e);
        }
    }
}

