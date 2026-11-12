package com.inventorymanagement.backend.application.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.inventorymanagement.backend.domain.entity.AuditLog;
import com.inventorymanagement.backend.infrastructure.persistence.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
}