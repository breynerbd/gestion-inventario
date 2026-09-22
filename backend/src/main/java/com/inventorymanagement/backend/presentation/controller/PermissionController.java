package com.inventorymanagement.backend.presentation.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventorymanagement.backend.application.dto.permission.PermissionCreateDTO;
import com.inventorymanagement.backend.application.dto.permission.PermissionStatusDTO;
import com.inventorymanagement.backend.application.dto.permission.PermissionResponseDTO;
import com.inventorymanagement.backend.application.dto.permission.PermissionUpdateDTO;
import com.inventorymanagement.backend.application.service.PermissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionController.class);

    @GetMapping
    public ResponseEntity<List<PermissionResponseDTO>> findAll(){
        LOGGER.info("Solicitud para obtener todas los permisos");
        return ResponseEntity.ok(service.findAllPermissions());
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionResponseDTO> findById(@PathVariable Long permissionId){
        LOGGER.info("Solicitud para obtener el permiso con id {}", permissionId);
        return ResponseEntity.ok(service.findPermissionById(permissionId));
    }

    @PostMapping
    public ResponseEntity<PermissionResponseDTO> create(@Valid @RequestBody PermissionCreateDTO permission){
        LOGGER.info("Solicitud para crear un permiso");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createPermission(permission));
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionResponseDTO> update(@PathVariable Long permissionId, @Valid @RequestBody PermissionUpdateDTO permissionDto){
        LOGGER.info("Solicitud para actualizar el permiso con id {}", permissionId);
        return ResponseEntity.ok(service.updatePermission(permissionId, permissionDto));
    }

    @PatchMapping("/{permissionId}/status")
    public ResponseEntity<PermissionResponseDTO> changeStatus(@PathVariable Long permissionId, @Valid @RequestBody PermissionStatusDTO statusDto) {
        LOGGER.info("Solicitud para cambiar el estado del permiso con id {}", permissionId);
        return ResponseEntity.ok(service.changeStatus(permissionId, statusDto.getStatus()));
    }
}