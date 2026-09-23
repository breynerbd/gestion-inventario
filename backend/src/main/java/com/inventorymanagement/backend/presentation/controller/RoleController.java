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

import com.inventorymanagement.backend.application.dto.role.RoleCreateDTO;
import com.inventorymanagement.backend.application.dto.role.RoleStatusDTO;
import com.inventorymanagement.backend.application.dto.role.RoleResponseDTO;
import com.inventorymanagement.backend.application.dto.role.RoleUpdateDTO;
import com.inventorymanagement.backend.application.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
    
    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> findAll(){
        LOGGER.info("Solicitud para obtener todos los roles");
        return ResponseEntity.ok(service.findAllRoles());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponseDTO> findById(@PathVariable Long roleId){
        LOGGER.info("Solicitud para obtener el rol con id {}", roleId);
        return ResponseEntity.ok(service.findRoleById(roleId));
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(@Valid @RequestBody RoleCreateDTO role){
        LOGGER.info("Solicitud para crear un rol");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRole(role));
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<RoleResponseDTO> update(@PathVariable Long roleId, @Valid @RequestBody RoleUpdateDTO roleDto){
        LOGGER.info("Solicitud para actualizar el rol con id {}", roleId);
        return ResponseEntity.ok(service.updateRole(roleId, roleDto));
    }

    @PatchMapping("/{roleId}/status")
    public ResponseEntity<RoleResponseDTO> changeStatus(@PathVariable Long roleId, @Valid @RequestBody RoleStatusDTO statusDto) {
        LOGGER.info("Solicitud para cambiar el estado del rol con id {}", roleId);
        return ResponseEntity.ok(service.changeStatus(roleId, statusDto.getStatus()));
    }
}