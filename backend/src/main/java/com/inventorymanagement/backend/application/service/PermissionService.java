package com.inventorymanagement.backend.application.service;

import com.inventorymanagement.backend.domain.entity.Permission;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.application.dto.permission.PermissionCreateDTO;
import com.inventorymanagement.backend.application.dto.permission.PermissionResponseDTO;
import com.inventorymanagement.backend.application.dto.permission.PermissionUpdateDTO;
import com.inventorymanagement.backend.application.mapper.PermissionMapper;
import com.inventorymanagement.backend.infrastructure.persistence.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository repository;
    private final PermissionMapper mapper;
    private static final String PERMISSION_NOT_FOUND = "El permiso ";
    private static final String PERMISSION_NOT_EXIST = " no existe";
    private static final String LOGGER_NOT_FOUND = "No se encontro el permiso: {}";

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionService.class);

    public List<PermissionResponseDTO> findAllPermissions() {
        LOGGER.debug("Obteniendo datos de permisos existentes");
        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public PermissionResponseDTO findPermissionById(Long permissionId) {
        LOGGER.debug("Buscando permiso: {}", permissionId);
        Permission permission = repository.findById(permissionId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, permissionId);
                return new ResourceNotFoundException(PERMISSION_NOT_FOUND + permissionId + PERMISSION_NOT_EXIST);}
            );

        return mapper.toResponseDTO(permission);
    }

    public PermissionResponseDTO createPermission(PermissionCreateDTO permissionDto) {
        LOGGER.debug("Creando Permiso");

        Permission permission = mapper.toEntity(permissionDto);

        Permission savedPermission = repository.save(permission);

        LOGGER.info("Se creo el permiso: {}", savedPermission.getPermissionId());

        return mapper.toResponseDTO(savedPermission);
    }

    public PermissionResponseDTO updatePermission(Long permissionId, PermissionUpdateDTO permissionDto) {
        LOGGER.debug("Actualizando permiso: {}", permissionId);

        Permission permission = repository.findById(permissionId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, permissionId);
                return new ResourceNotFoundException(PERMISSION_NOT_FOUND + permissionId + PERMISSION_NOT_EXIST);}
            );

        mapper.updateEntity(permissionDto, permission);

        Permission updatedPermission = repository.save(permission);

        LOGGER.info("El permiso {} se ha actualizado", permissionId);

        return mapper.toResponseDTO(updatedPermission);
    }

    public PermissionResponseDTO changeStatus(Long permissionId, Permission.Status status) {
        LOGGER.debug("Cambiando estado del permiso {} a {}", permissionId, status);

        Permission permission = repository.findById(permissionId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, permissionId);
                return new ResourceNotFoundException(PERMISSION_NOT_FOUND + permissionId + PERMISSION_NOT_EXIST);}
            );

        if (permission.getStatus() == status) {
            String message = switch (status) {
                case ACTIVO -> "El permiso ya esta ACTIVO";
                case INACTIVO -> "El permiso ya esta INACTIVO";
            };

            throw new StatusUnchangedException(message);
        }

        permission.setStatus(status);

        Permission updatedPermission = repository.save(permission);
        
        LOGGER.info("El estado del permiso {} se ha cambiado a {}", permissionId, status);
        return mapper.toResponseDTO(updatedPermission);
    }
}