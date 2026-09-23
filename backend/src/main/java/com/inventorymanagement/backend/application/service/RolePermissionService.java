package com.inventorymanagement.backend.application.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventorymanagement.backend.application.dto.rolePermission.RolePermissionDTO;
import com.inventorymanagement.backend.application.dto.rolePermission.RolePermissionResponseDTO;
import com.inventorymanagement.backend.application.mapper.RolePermissionMapper;
import com.inventorymanagement.backend.domain.entity.Permission;
import com.inventorymanagement.backend.domain.entity.Role;
import com.inventorymanagement.backend.domain.entity.RolePermission;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.infrastructure.persistence.repository.PermissionRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.RolePermissionRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolePermissionService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permisoRepository;
    private final RolePermissionRepository rolPermisoRepository;
    private final RolePermissionMapper rolPermisoMapper;
    private static final ZoneId ZONE_ID = ZoneId.of("America/Guatemala");
    private static final String NOT_EXIST = " no existe";

    private static final String LOGGER_NOT_FOUND = "No se encontro el rol: {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(RolePermissionService.class);

    public List<RolePermissionResponseDTO> findAllRolePermissions(Long roleId) {
        LOGGER.debug("Obteniendo permisos del rol: {}", roleId);

        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, roleId);
                return new ResourceNotFoundException("El rol " + roleId + NOT_EXIST);}
            );

        return rolPermisoRepository.findByRoleRoleId(role.getRoleId()).stream().map(rolPermisoMapper::toResponseDTO).toList();
    }

    @Transactional
    public List<RolePermissionResponseDTO> assignPermissions(Long roleId, RolePermissionDTO dto) {
        LOGGER.debug("Asignando permisos al rol: {}", roleId);

        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, roleId);
                return new ResourceNotFoundException("El rol " + roleId + NOT_EXIST);
            });

        if (role.getStatus() == Role.Status.INACTIVO) {
            throw new IllegalArgumentException("No se pueden asignar permisos a un rol INACTIVO");
        }

        List<Long> uniquePermissionIds = dto.getPermissionIds().stream().distinct().toList();

        List<Permission> permissions = uniquePermissionIds.stream().map(permissionId -> permisoRepository.findById(permissionId)
            .orElseThrow(() -> {
                LOGGER.warn("No se encontro el permiso: {}", permissionId);
                return new ResourceNotFoundException("El permiso " + permissionId + NOT_EXIST);
            })).toList();
        
        for (Permission permission : permissions) {
            if (permission.getStatus() == Permission.Status.INACTIVO) {
                throw new IllegalArgumentException("El permiso " + permission.getPermissionId() + " esta INACTIVO");
            }
        }

        rolPermisoRepository.deleteByRoleRoleId(roleId);

        for (Permission permission : permissions) {

            RolePermission relation = new RolePermission();

            relation.setId(new RolePermission.RolePermissionId(role.getRoleId(),permission.getPermissionId()));

            relation.setRole(role);
            relation.setPermission(permission);
            relation.setAssignmentDate(LocalDateTime.now(ZONE_ID));

            rolPermisoRepository.save(relation);
        }

        LOGGER.info("Se asignaron los permisos al rol: {}", roleId);

        return findAllRolePermissions(roleId);
    }
}