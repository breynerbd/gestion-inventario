package com.inventorymanagement.backend.application.service;

import com.inventorymanagement.backend.domain.entity.Role;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.application.dto.role.RoleCreateDTO;
import com.inventorymanagement.backend.application.dto.role.RoleResponseDTO;
import com.inventorymanagement.backend.application.dto.role.RoleUpdateDTO;
import com.inventorymanagement.backend.application.mapper.RoleMapper;
import com.inventorymanagement.backend.infrastructure.persistence.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;
    private static final String ROLE_NOT_FOUND = "El rol ";
    private static final String NOT_EXIST = " no existe";

    private static final String LOGGER_NOT_FOUND = "No se encontro el rol: {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

    public List<RoleResponseDTO> findAllRoles() {
        LOGGER.debug("Obteniendo datos de roles existentes");
        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public RoleResponseDTO findRoleById(Long roleId) {
        LOGGER.debug("Buscando rol: {}", roleId);

        Role role = repository.findById(roleId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, roleId);
                return new ResourceNotFoundException(ROLE_NOT_FOUND + roleId + NOT_EXIST);}
            );

        return mapper.toResponseDTO(role);
    }

    public RoleResponseDTO createRole(RoleCreateDTO roleDto) {
        LOGGER.debug("Creando un rol");

        Role role = mapper.toEntity(roleDto);

        Role savedRole = repository.save(role);

        LOGGER.info("Se creo el rol con id: {}", savedRole.getRoleId());

        return mapper.toResponseDTO(savedRole);
    }

    public RoleResponseDTO updateRole(Long roleId, RoleUpdateDTO roleDto) {
        LOGGER.debug("Actualizando rol: {}", roleId);

        Role role = repository.findById(roleId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, roleId);
                return new ResourceNotFoundException(ROLE_NOT_FOUND + roleId + NOT_EXIST);
            });

        mapper.updateEntity(roleDto, role);

        Role updatedRole = repository.save(role);

        LOGGER.info("El rol {} se ha actualizado", roleId);

        return mapper.toResponseDTO(updatedRole);
    }

    public RoleResponseDTO changeStatus(Long roleId, Role.Status status) {
        LOGGER.debug("Cambiando estado del rol {} a {}", roleId, status);

        Role role = repository.findById(roleId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, roleId);
                return new ResourceNotFoundException(ROLE_NOT_FOUND + roleId + NOT_EXIST);
            });

        if (role.getStatus() == status) {
            String menssage = switch (status) {
                case ACTIVO -> "El rol ya esta ACTIVO";
                case INACTIVO -> "El rol ya esta INACTIVO";
            };

            throw new StatusUnchangedException(menssage);
        }

        role.setStatus(status);

        Role updatedRole = repository.save(role);

        LOGGER.info("El estado del rol {} se ha cambiado a {}", roleId, status);

        return mapper.toResponseDTO(updatedRole);
    }
}