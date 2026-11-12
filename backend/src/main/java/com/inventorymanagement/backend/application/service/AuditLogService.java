package com.inventorymanagement.backend.application.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.inventorymanagement.backend.domain.entity.AuditLog;
import com.inventorymanagement.backend.infrastructure.persistence.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.inventorymanagement.backend.application.dto.audit.AuditLogResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private static final ZoneId ZONE_ID =  ZoneId.of("America/Guatemala");

    private final AuditLogRepository repository;
    private final CurrentUserService currentUserService;

    @Transactional(propagation = Propagation.MANDATORY)
    public void register(AuditLog.Module module, Long recordId, AuditLog.Action action) {

        AuditLog log = new AuditLog();
        log.setUser(currentUserService.getCurrentUser());
        log.setModule(module);
        log.setRecordId(recordId);
        log.setAction(action);
        log.setOccurredAt(LocalDateTime.now(ZONE_ID));

        repository.save(log);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogResponseDTO> findAllLogs(Pageable pageable) {
        return repository.findAll(pageable).map(log -> {
            AuditLogResponseDTO dto = new AuditLogResponseDTO();

            dto.setAuditId(log.getAuditId());
            dto.setUsername(log.getUser().getUsername());
            dto.setModule(log.getModule());
            dto.setRecordId(log.getRecordId());
            dto.setAction(log.getAction());
            dto.setOccurredAt(log.getOccurredAt());

            return dto;
        });
    }
}