package com.gestioninventario.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.application.dto.role.RoleCreateDTO;
import com.gestioninventario.backend.application.dto.role.RoleResponseDTO;
import com.gestioninventario.backend.application.dto.role.RoleUpdateDTO;
import com.gestioninventario.backend.domain.entity.Role;

@Component
public class RoleMapper {

    public Role toEntity(RoleCreateDTO dto) {

        Role role = new Role();

        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());
        role.setStatus(Role.Status.ACTIVO);

        return role;
    }

    public void updateEntity(RoleUpdateDTO dto, Role role) {
        
        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());
    }

    public RoleResponseDTO toResponseDTO(Role role) {

        RoleResponseDTO dto = new RoleResponseDTO();

        dto.setRoleId(role.getRoleId());
        dto.setRoleName(role.getRoleName());
        dto.setDescription(role.getDescription());
        dto.setStatus(role.getStatus());

        return dto;
    }
}