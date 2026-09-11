package com.gestioninventario.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.application.dto.rolePermission.RolePermissionResponseDTO;
import com.gestioninventario.backend.domain.entity.Permission;
import com.gestioninventario.backend.domain.entity.RolePermission;

@Component
public class RolePermissionMapper {

    public RolePermissionResponseDTO toResponseDTO(RolePermission rolePermission) {
        
        RolePermissionResponseDTO dto = new RolePermissionResponseDTO();

        Permission permission = rolePermission.getPermission();

        dto.setPermissionId(permission.getPermissionId());
        dto.setPermissionCode(permission.getPermissionCode());
        dto.setPermissionName(permission.getPermissionName());
        dto.setModule(permission.getModule());
        dto.setDescription(permission.getDescription());
        dto.setStatus(permission.getStatus());
        dto.setAssignmentDate(rolePermission.getAssignmentDate());

        return dto;
    }
}