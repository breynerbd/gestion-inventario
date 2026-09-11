package com.gestioninventario.backend.application.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestioninventario.backend.application.dto.rolePermission.RolePermissionDTO;
import com.gestioninventario.backend.application.dto.rolePermission.RolePermissionResponseDTO;
import com.gestioninventario.backend.application.mapper.RolePermissionMapper;
import com.gestioninventario.backend.domain.entity.Permission;
import com.gestioninventario.backend.domain.entity.Role;
import com.gestioninventario.backend.domain.entity.RolePermission;
import com.gestioninventario.backend.domain.exception.ResourceNotFoundException;
import com.gestioninventario.backend.infrastructure.persistence.repository.PermissionRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.RolePermissionRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.RoleRepository;

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

    public List<RolePermissionResponseDTO> findAllRolePermissions(Long roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("El rol " + roleId + NOT_EXIST));

        return rolPermisoRepository.findByRoleRoleId(role.getRoleId()).stream().map(rolPermisoMapper::toResponseDTO).toList();
    }

    @Transactional
    public List<RolePermissionResponseDTO> assignPermissions(Long roleId, RolePermissionDTO dto) {

        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("El rol " + roleId + NOT_EXIST));

        if (role.getStatus() == Role.Status.INACTIVO) {
            throw new IllegalArgumentException("No se pueden asignar permisos a un rol INACTIVO");
        }

        List<Long> uniquePermissionIds = dto.getPermissionIds().stream().distinct().toList();

        List<Permission> permissions = uniquePermissionIds.stream()
            .map(permissionId -> permisoRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("El permiso " + permissionId + NOT_EXIST))).toList();

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

        return findAllRolePermissions(roleId);
    }
}