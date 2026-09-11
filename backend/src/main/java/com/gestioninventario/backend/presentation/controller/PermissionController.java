package com.gestioninventario.backend.presentation.controller;

import java.util.List;

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

import com.gestioninventario.backend.application.dto.permission.PermissionCreateDTO;
import com.gestioninventario.backend.application.dto.permission.PermissionStatusDTO;
import com.gestioninventario.backend.application.dto.permission.PermissionResponseDTO;
import com.gestioninventario.backend.application.dto.permission.PermissionUpdateDTO;
import com.gestioninventario.backend.application.service.PermissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService service;

    @GetMapping
    public ResponseEntity<List<PermissionResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAllPermissions());
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionResponseDTO> findById(@PathVariable Long permissionId){
        return ResponseEntity.ok(service.findPermissionById(permissionId));
    }

    @PostMapping
    public ResponseEntity<PermissionResponseDTO> create(@Valid @RequestBody PermissionCreateDTO permission){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createPermission(permission));
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionResponseDTO> update(@PathVariable Long permissionId, @Valid @RequestBody PermissionUpdateDTO permissionDto){
        return ResponseEntity.ok(service.updatePermission(permissionId, permissionDto));
    }

    @PatchMapping("/{permissionId}/status")
    public ResponseEntity<PermissionResponseDTO> changeStatus(@PathVariable Long permissionId, @Valid @RequestBody PermissionStatusDTO statusDto) {
        return ResponseEntity.ok(service.changeStatus(permissionId, statusDto.getStatus()));
    }
}