package com.inventorymanagement.backend.presentation.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventorymanagement.backend.application.dto.rolePermission.RolePermissionDTO;
import com.inventorymanagement.backend.application.dto.rolePermission.RolePermissionResponseDTO;
import com.inventorymanagement.backend.application.service.RolePermissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/roles/{roleId}/permissions")
public class RolePermissionController {

    private final RolePermissionService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(RolePermissionController.class);
    
    @GetMapping
    public ResponseEntity<List<RolePermissionResponseDTO>> findAll(@PathVariable Long roleId) {
        LOGGER.info("Solicitud para obtener todos los permisos segun rol");
        return ResponseEntity.ok(service.findAllRolePermissions(roleId));
    }

    @PutMapping
    public ResponseEntity<List<RolePermissionResponseDTO>> assignPermissions(@PathVariable Long roleId,@Valid @RequestBody RolePermissionDTO permissionDto) {
        LOGGER.info("Solicitud para asignar permisos a un rol");
        return ResponseEntity.ok(service.assignPermissions(roleId, permissionDto));
    }
}