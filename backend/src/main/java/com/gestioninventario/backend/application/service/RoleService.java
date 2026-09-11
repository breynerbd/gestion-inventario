package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.domain.entity.Role;
import com.gestioninventario.backend.domain.exception.StatusUnchangedException;
import com.gestioninventario.backend.domain.exception.ResourceNotFoundException;
import com.gestioninventario.backend.application.dto.role.RoleCreateDTO;
import com.gestioninventario.backend.application.dto.role.RoleResponseDTO;
import com.gestioninventario.backend.application.dto.role.RoleUpdateDTO;
import com.gestioninventario.backend.application.mapper.RoleMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;
    private static final String ROLE_NOT_FOUND = "El rol ";
    private static final String NOT_EXIST = " no existe";

    public List<RoleResponseDTO> findAllRoles() {
        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public RoleResponseDTO findRoleById(Long roleId) {

        Role role = repository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND + roleId + NOT_EXIST));

        return mapper.toResponseDTO(role);
    }

    public RoleResponseDTO createRole(RoleCreateDTO roleDto) {
        Role role = mapper.toEntity(roleDto);

        Role savedRole = repository.save(role);

        return mapper.toResponseDTO(savedRole);
    }

    public RoleResponseDTO updateRole(Long roleId, RoleUpdateDTO roleDto) {

        Role role = repository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND + roleId + NOT_EXIST));

        mapper.updateEntity(roleDto, role);

        Role updatedRole = repository.save(role);

        return mapper.toResponseDTO(updatedRole);
    }

    public RoleResponseDTO changeStatus(Long roleId, Role.Status status) {
        Role role = repository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND + roleId + NOT_EXIST));

        if (role.getStatus() == status) {
            String menssage = switch (status) {
                case ACTIVO -> "El rol ya esta ACTIVO";
                case INACTIVO -> "El rol ya esta INACTIVO";
            };

            throw new StatusUnchangedException(menssage);
        }

        role.setStatus(status);

        Role updatedRole = repository.save(role);

        return mapper.toResponseDTO(updatedRole);
    }
}