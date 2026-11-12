package com.inventorymanagement.backend.presentation.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventorymanagement.backend.application.dto.binnacle.BinnacleResponseDTO;
import com.inventorymanagement.backend.application.service.BinnacleService;
import com.inventorymanagement.backend.application.dto.audit.AuditLogResponseDTO;
import com.inventorymanagement.backend.application.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binnacle")
public class BinnacleController {
    
    private final BinnacleService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(BinnacleController.class);
    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<BinnacleResponseDTO>> findAll() {
        LOGGER.info("Solicitud para obtener datos de bitacora");
        return ResponseEntity.ok(service.findAllLogs());
    }

    @GetMapping("/changes")
    public ResponseEntity<Page<AuditLogResponseDTO>> findChanges(
            @PageableDefault(
                    size = 12,
                    sort = "auditId",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(auditLogService.findAllLogs(pageable));
    }
}
