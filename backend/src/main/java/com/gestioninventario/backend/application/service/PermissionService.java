package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.domain.entity.Permission;
import com.gestioninventario.backend.domain.exception.StatusUnchangedException;
import com.gestioninventario.backend.domain.exception.ResourceNotFoundException;
import com.gestioninventario.backend.application.dto.permission.PermissionCreateDTO;
import com.gestioninventario.backend.application.dto.permission.PermissionResponseDTO;
import com.gestioninventario.backend.application.dto.permission.PermissionUpdateDTO;
import com.gestioninventario.backend.application.mapper.PermissionMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository repository;
    private final PermissionMapper mapper;
    private static final String PERMISSION_NOT_FOUND = "El permiso ";
    private static final String PERMISSION_NOT_EXIST = " no existe";

    public List<PermissionResponseDTO> findAllPermissions() {
        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public PermissionResponseDTO findPermissionById(Long permissionId) {
        Permission permission = repository.findById(permissionId)
            .orElseThrow(() -> new ResourceNotFoundException(PERMISSION_NOT_FOUND + permissionId + PERMISSION_NOT_EXIST));

        return mapper.toResponseDTO(permission);
    }

    public PermissionResponseDTO createPermission(PermissionCreateDTO permissionDto) {

        Permission permission = mapper.toEntity(permissionDto);

        Permission savedPermission = repository.save(permission);

        return mapper.toResponseDTO(savedPermission);
    }

    public PermissionResponseDTO updatePermission(Long permissionId, PermissionUpdateDTO permissionDto) {

        Permission permission = repository.findById(permissionId)
            .orElseThrow(() -> new ResourceNotFoundException(PERMISSION_NOT_FOUND + permissionId + PERMISSION_NOT_EXIST));

        mapper.updateEntity(permissionDto, permission);

        Permission updatedPermission = repository.save(permission);

        return mapper.toResponseDTO(updatedPermission);
    }

    public PermissionResponseDTO changeStatus(Long permissionId, Permission.Status status) {
        Permission permission = repository.findById(permissionId)
            .orElseThrow(() -> new ResourceNotFoundException(PERMISSION_NOT_FOUND + permissionId + PERMISSION_NOT_EXIST));

        if (permission.getStatus() == status) {
            String message = switch (status) {
                case ACTIVO -> "El permiso ya esta ACTIVO";
                case INACTIVO -> "El permiso ya esta INACTIVO";
            };

            throw new StatusUnchangedException(message);
        }

        permission.setStatus(status);

        Permission updatedPermission = repository.save(permission);

        return mapper.toResponseDTO(updatedPermission);
    }
}