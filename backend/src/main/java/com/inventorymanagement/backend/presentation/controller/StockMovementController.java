package com.inventorymanagement.backend.presentation.controller;

import com.inventorymanagement.backend.application.dto.movement.StockMovementCreateDTO;
import com.inventorymanagement.backend.application.dto.movement.StockMovementStatusDTO;
import com.inventorymanagement.backend.application.dto.movement.StockMovementResponseDTO;
import com.inventorymanagement.backend.application.dto.movement.StockMovementUpdateDTO;
import com.inventorymanagement.backend.application.service.StockMovementService;
import com.inventorymanagement.backend.domain.entity.StockMovement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/movements")
public class StockMovementController {

    private final StockMovementService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(StockMovementController.class);

    @GetMapping
    public ResponseEntity<Page<StockMovementResponseDTO>> findAll(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) StockMovement.MovementType movementType,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long userId,
            @PageableDefault(size = 10) Pageable pageable) {

        LOGGER.info("Solicitud para obtener todos los movimientos de stock");
        return ResponseEntity.ok(service.findAllMovements(productId, movementType, startDate, endDate, userId, pageable));
    }

    @GetMapping("/{movementId}")
    public ResponseEntity<StockMovementResponseDTO> findById(@PathVariable Long movementId) {
        LOGGER.info("Solicitud para obtener el movimiento con id {}", movementId);
        return ResponseEntity.ok(service.findMovementById(movementId));
    }

    @PostMapping
    public ResponseEntity<StockMovementResponseDTO> create(@Valid @RequestBody StockMovementCreateDTO movement, Authentication authentication) {
        LOGGER.info("Solicitud para crear un movimiento en stock");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerMovement(movement, authentication.getName()));
    }

    @PutMapping("/{movementId}")
    public ResponseEntity<StockMovementResponseDTO> update(@PathVariable Long movementId, @Valid @RequestBody StockMovementUpdateDTO movementDto) {
        LOGGER.info("Solicitud para actualizar el movimiento con id {}", movementId);
        return ResponseEntity.ok(service.updateMovement(movementId, movementDto));
    }

    @PatchMapping("/{movementId}/status")
    public ResponseEntity<StockMovementResponseDTO> changeStatus(@PathVariable Long movementId, @Valid @RequestBody StockMovementStatusDTO statusDto) {
        LOGGER.info("Solicitud para cambiar el estado del movimiento con id {}", movementId);
        return ResponseEntity.ok(service.changeStatus(movementId, statusDto.getStatus()));
    }
}