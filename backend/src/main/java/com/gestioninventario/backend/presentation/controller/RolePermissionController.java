package com.gestioninventario.backend.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestioninventario.backend.application.dto.rolePermission.RolePermissionDTO;
import com.gestioninventario.backend.application.dto.rolePermission.RolePermissionResponseDTO;
import com.gestioninventario.backend.application.service.RolePermissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/roles/{roleId}/permissions")
public class RolePermissionController {

    private final RolePermissionService service;

    @GetMapping
    public ResponseEntity<List<RolePermissionResponseDTO>> findAll(@PathVariable Long roleId) {
        return ResponseEntity.ok(service.findAllRolePermissions(roleId));
    }

    @PutMapping
    public ResponseEntity<List<RolePermissionResponseDTO>> assignPermissions(@PathVariable Long roleId,@Valid @RequestBody RolePermissionDTO permissionDto) {
        return ResponseEntity.ok(service.assignPermissions(roleId, permissionDto));
    }
}