package com.inventorymanagement.backend.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inventorymanagement.backend.application.dto.permission.PermissionCreateDTO;
import com.inventorymanagement.backend.application.dto.permission.PermissionResponseDTO;
import com.inventorymanagement.backend.application.dto.permission.PermissionUpdateDTO;
import com.inventorymanagement.backend.application.mapper.PermissionMapper;
import com.inventorymanagement.backend.domain.entity.Permission;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.infrastructure.persistence.repository.PermissionRepository;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @Mock
    private PermissionRepository repository;

    @Mock
    private PermissionMapper mapper;

    @InjectMocks
    private PermissionService service;

    private Permission permission;
    private PermissionResponseDTO response;
    private PermissionCreateDTO permissionDTO;
    private PermissionUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        permission = new Permission();
        permission.setPermissionId(1L);
        permission.setPermissionCode("PRODUCTOS_CREAR");
        permission.setPermissionName("Crear productos");
        permission.setModule(Permission.Module.PRODUCTOS);
        permission.setDescription("Permite crear productos");
        permission.setStatus(Permission.Status.ACTIVO);

        response = new PermissionResponseDTO();
        response.setPermissionId(1L);
        response.setPermissionCode("PRODUCTOS_CREAR");
        response.setPermissionName("Crear productos");
        response.setModule(Permission.Module.PRODUCTOS);
        response.setDescription("Permite crear productos");
        response.setStatus(Permission.Status.ACTIVO);

        permissionDTO = new PermissionCreateDTO();
        permissionDTO.setPermissionCode("PRODUCTOS_EDITAR");
        permissionDTO.setPermissionName("Editar productos");
        permissionDTO.setModule(Permission.Module.PRODUCTOS);
        permissionDTO.setDescription("Permite editar productos");

        updateDTO = new PermissionUpdateDTO();
        updateDTO.setPermissionName("Editar productos");
        updateDTO.setModule(Permission.Module.PRODUCTOS);
        updateDTO.setDescription("Permite editar productos");
    }

    @Test
    void findPermissionById() {
        when(repository.findById(1L)).thenReturn(Optional.of(permission));
        when(mapper.toResponseDTO(permission)).thenReturn(response);

        PermissionResponseDTO result = service.findPermissionById(1L);

        assertNotNull(result);
        assertEquals("PRODUCTOS_CREAR", result.getPermissionCode());
        assertEquals(Permission.Status.ACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(mapper).toResponseDTO(permission);
    }

    @Test
    void findPermissionByIdReturnException() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findPermissionById(10L));

        assertEquals("El permiso 10 no existe", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void findAllPermissions() {
        when(repository.findAll()).thenReturn(List.of(permission));
        when(mapper.toResponseDTO(permission)).thenReturn(response);

        List<PermissionResponseDTO> result = service.findAllPermissions();

        assertEquals(1, result.size());
        assertEquals("PRODUCTOS_CREAR", result.get(0).getPermissionCode());

        verify(repository).findAll();
        verify(mapper).toResponseDTO(permission);
    }

    @Test
    void createPermission() {
        Permission saved = new Permission();
        saved.setPermissionId(2L);
        saved.setPermissionCode("PRODUCTOS_EDITAR");
        saved.setPermissionName("Editar productos");
        saved.setModule(Permission.Module.PRODUCTOS);
        saved.setDescription("Permite editar productos");
        saved.setStatus(Permission.Status.ACTIVO);

        response.setPermissionId(2L);
        response.setPermissionCode("PRODUCTOS_EDITAR");
        response.setPermissionName("Editar productos");
        response.setDescription("Permite editar productos");

        when(mapper.toEntity(permissionDTO)).thenReturn(permission);
        when(repository.save(permission)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(response);

        PermissionResponseDTO result = service.createPermission(permissionDTO);

        assertNotNull(result);
        assertEquals(2L, result.getPermissionId());
        assertEquals("PRODUCTOS_EDITAR", result.getPermissionCode());

        verify(mapper).toEntity(permissionDTO);
        verify(repository).save(permission);
        verify(mapper).toResponseDTO(saved);
    }

    @Test
    void updatePermission() {
        when(repository.findById(1L)).thenReturn(Optional.of(permission));
        when(repository.save(permission)).thenReturn(permission);
        when(mapper.toResponseDTO(permission)).thenReturn(response);

        PermissionResponseDTO result = service.updatePermission(1L, updateDTO);

        assertNotNull(result);

        verify(mapper).updateEntity(updateDTO, permission);
        verify(repository).save(permission);
        verify(mapper).toResponseDTO(permission);
    }

    @Test
    void updatePermissionNotExists() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updatePermission(10L, updateDTO));

        assertEquals("El permiso 10 no existe", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusInactive() {
        response.setStatus(Permission.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(permission));
        when(repository.save(permission)).thenReturn(permission);
        when(mapper.toResponseDTO(permission)).thenReturn(response);

        PermissionResponseDTO result = service.changeStatus(1L, Permission.Status.INACTIVO);

        assertNotNull(result);
        assertEquals(Permission.Status.INACTIVO, permission.getStatus());
        assertEquals(Permission.Status.INACTIVO, result.getStatus());

        verify(repository).save(permission);
    }

    @Test
    void changeStatusActive() {
        permission.setStatus(Permission.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(permission));
        when(repository.save(permission)).thenReturn(permission);
        when(mapper.toResponseDTO(permission)).thenReturn(response);

        PermissionResponseDTO result = service.changeStatus(1L, Permission.Status.ACTIVO);

        assertNotNull(result);
        assertEquals(Permission.Status.ACTIVO, permission.getStatus());
        assertEquals(Permission.Status.ACTIVO, result.getStatus());

        verify(repository).save(permission);
    }

    @Test
    void changeStatusWhenAlreadyActive() {
        when(repository.findById(1L)).thenReturn(Optional.of(permission));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Permission.Status.ACTIVO));

        assertEquals("El permiso ya esta ACTIVO", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWhenAlreadyInactive() {
        permission.setStatus(Permission.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(permission));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Permission.Status.INACTIVO));

        assertEquals("El permiso ya esta INACTIVO", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWhenPermissionNotExist() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.changeStatus(10L, Permission.Status.INACTIVO));

        assertEquals("El permiso 10 no existe", exception.getMessage());

        verifyNoInteractions(mapper);
    }
}