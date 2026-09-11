package com.gestioninventario.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.application.dto.permission.PermissionCreateDTO;
import com.gestioninventario.backend.application.dto.permission.PermissionResponseDTO;
import com.gestioninventario.backend.application.dto.permission.PermissionUpdateDTO;
import com.gestioninventario.backend.domain.entity.Permission;

@Component
public class PermissionMapper {

    public Permission toEntity(PermissionCreateDTO dto) {

        Permission permission = new Permission();

        permission.setPermissionCode(dto.getPermissionCode());
        permission.setPermissionName(dto.getPermissionName());
        permission.setModule(dto.getModule());
        permission.setDescription(dto.getDescription());
        permission.setStatus(Permission.Status.ACTIVO);

        return permission;
    }

    public void updateEntity(PermissionUpdateDTO dto, Permission permission) {

        permission.setPermissionName(dto.getPermissionName());
        permission.setModule(dto.getModule());
        permission.setDescription(dto.getDescription());
    }

    public PermissionResponseDTO toResponseDTO(Permission permission) {

        PermissionResponseDTO dto = new PermissionResponseDTO();

        dto.setPermissionId(permission.getPermissionId());
        dto.setPermissionCode(permission.getPermissionCode());
        dto.setPermissionName(permission.getPermissionName());
        dto.setModule(permission.getModule());
        dto.setDescription(permission.getDescription());
        dto.setStatus(permission.getStatus());

        return dto;
    }
}