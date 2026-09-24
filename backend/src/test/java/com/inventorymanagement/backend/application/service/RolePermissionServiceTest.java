package com.inventorymanagement.backend.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class RolePermissionServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permisoRepository;

    @Mock
    private RolePermissionRepository rolPermisoRepository;

    @Mock
    private RolePermissionMapper rolPermisoMapper;

    @InjectMocks
    private RolePermissionService service;

    private Role role;
    private Permission permission;
    private RolePermission rolePermission;
    private RolePermissionDTO dto;
    private RolePermissionResponseDTO response;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleId(1L);
        role.setStatus(Role.Status.ACTIVO);

        permission = new Permission();
        permission.setPermissionId(2L);
        permission.setStatus(Permission.Status.ACTIVO);

        rolePermission = new RolePermission();
        rolePermission.setId(new RolePermission.RolePermissionId(1L, 2L));
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);

        dto = new RolePermissionDTO();
        dto.setPermissionIds(List.of(2L));

        response = new RolePermissionResponseDTO();
        response.setPermissionId(2L);
        response.setStatus(Permission.Status.ACTIVO);
    }

    @Test
    void findAllRolePermissions() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(rolPermisoRepository.findByRoleRoleId(1L)).thenReturn(List.of(rolePermission));
        when(rolPermisoMapper.toResponseDTO(rolePermission)).thenReturn(response);

        List<RolePermissionResponseDTO> result = service.findAllRolePermissions(1L);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getPermissionId());

        verify(roleRepository).findById(1L);
        verify(rolPermisoRepository).findByRoleRoleId(1L);
        verify(rolPermisoMapper).toResponseDTO(rolePermission);
    }

    @Test
    void findAllRolePermissionsRoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findAllRolePermissions(1L));

        assertEquals("El rol 1 no existe", exception.getMessage());

        verify(roleRepository).findById(1L);
    }

    @Test
    void assignPermissions() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(permisoRepository.findById(2L)).thenReturn(Optional.of(permission));
        when(rolPermisoRepository.findByRoleRoleId(1L)).thenReturn(List.of(rolePermission));
        when(rolPermisoMapper.toResponseDTO(rolePermission)).thenReturn(response);

        List<RolePermissionResponseDTO> result = service.assignPermissions(1L, dto);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getPermissionId());

        verify(roleRepository, times(2)).findById(1L);
        verify(permisoRepository).findById(2L);
        verify(rolPermisoRepository).deleteByRoleRoleId(1L);
        verify(rolPermisoRepository).findByRoleRoleId(1L);
        verify(rolPermisoMapper).toResponseDTO(rolePermission);
    }

    @Test
    void assignPermissionsRoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.assignPermissions(1L, dto));

        assertEquals("El rol 1 no existe", exception.getMessage());

        verify(roleRepository).findById(1L);
    }

    @Test
    void assignPermissionsInactiveRole() {
        role.setStatus(Role.Status.INACTIVO);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.assignPermissions(1L, dto));

        assertEquals("No se pueden asignar permisos a un rol INACTIVO", exception.getMessage());

        verify(roleRepository).findById(1L);
    }

    @Test
    void assignPermissionsPermissionNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(permisoRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.assignPermissions(1L, dto));

        assertEquals("El permiso 2 no existe", exception.getMessage());

        verify(roleRepository).findById(1L);
        verify(permisoRepository).findById(2L);
    }

    @Test
    void assignPermissionsInactivePermission() {
        permission.setStatus(Permission.Status.INACTIVO);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(permisoRepository.findById(2L)).thenReturn(Optional.of(permission));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.assignPermissions(1L, dto));

        assertEquals("El permiso 2 esta INACTIVO", exception.getMessage());

        verify(roleRepository).findById(1L);
        verify(permisoRepository).findById(2L);
    }
}