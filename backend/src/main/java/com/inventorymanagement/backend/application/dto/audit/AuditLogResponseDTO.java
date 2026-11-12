package com.inventorymanagement.backend.application.dto.audit;

import java.time.LocalDateTime;

import com.inventorymanagement.backend.domain.entity.AuditLog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuditLogResponseDTO {

    private Long auditId;
    private String username;
    private AuditLog.Module module;
    private Long recordId;
    private AuditLog.Action action;
    private LocalDateTime occurredAt;
}