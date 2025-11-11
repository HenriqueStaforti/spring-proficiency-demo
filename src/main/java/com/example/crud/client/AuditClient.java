package com.example.crud.client;

import com.example.crud.dto.AuditEventRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "audit-service", url = "${app.services.audit.url}")
public interface AuditClient {

    @PostMapping("/api/audit")
    void sendAuditEvent(@RequestBody AuditEventRequestDTO auditEventRequestDTO);
}
