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

import com.gestioninventario.backend.application.dto.role.RoleCreateDTO;
import com.gestioninventario.backend.application.dto.role.RoleStatusDTO;
import com.gestioninventario.backend.application.dto.role.RoleResponseDTO;
import com.gestioninventario.backend.application.dto.role.RoleUpdateDTO;
import com.gestioninventario.backend.application.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService service;

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAllRoles());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponseDTO> findById(@PathVariable Long roleId){
        return ResponseEntity.ok(service.findRoleById(roleId));
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(@Valid @RequestBody RoleCreateDTO role){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRole(role));
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<RoleResponseDTO> update(@PathVariable Long roleId, @Valid @RequestBody RoleUpdateDTO roleDto){
        return ResponseEntity.ok(service.updateRole(roleId, roleDto));
    }

    @PatchMapping("/{roleId}/status")
    public ResponseEntity<RoleResponseDTO> changeStatus(@PathVariable Long roleId, @Valid @RequestBody RoleStatusDTO statusDto) {
        return ResponseEntity.ok(service.changeStatus(roleId, statusDto.getStatus()));
    }
}