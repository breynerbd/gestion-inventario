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

    @BeforeEach
    void setUp() {
        permission = new Permission();
        permission.setPermissionId(1L);
        permission.setPermissionCode("PRODUCTOS_CREAR");
        permission.setPermissionName("Crear productos");
        permission.setModule(Permission.Module.PRODUCTOS);
        permission.setDescription("Permite crear productos");
        permission.setStatus(Permission.Status.ACTIVO);
    }

    @Test
    void findPermissionById() {
        PermissionResponseDTO response = new PermissionResponseDTO();
        response.setPermissionId(1L);
        response.setPermissionCode("PRODUCTOS_CREAR");
        response.setPermissionName("Crear productos");
        response.setModule(Permission.Module.PRODUCTOS);
        response.setDescription("Permite crear productos");
        response.setStatus(Permission.Status.ACTIVO);

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

        verify(repository).findById(10L);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAllPermissions() {
        PermissionResponseDTO response = new PermissionResponseDTO();
        response.setPermissionId(1L);
        response.setPermissionCode("PRODUCTOS_CREAR");
        response.setPermissionName("Crear productos");
        response.setModule(Permission.Module.PRODUCTOS);
        response.setDescription("Permite crear productos");
        response.setStatus(Permission.Status.ACTIVO);

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
        PermissionCreateDTO permissionDTO = new PermissionCreateDTO();
        permissionDTO.setPermissionCode("PRODUCTOS_EDITAR");
        permissionDTO.setPermissionName("Editar productos");
        permissionDTO.setModule(Permission.Module.PRODUCTOS);
        permissionDTO.setDescription("Permite editar productos");

        Permission newPermission = new Permission();
        newPermission.setPermissionCode("PRODUCTOS_EDITAR");
        newPermission.setPermissionName("Editar productos");
        newPermission.setModule(Permission.Module.PRODUCTOS);
        newPermission.setDescription("Permite editar productos");

        Permission saved = new Permission();
        saved.setPermissionId(2L);
        saved.setPermissionCode("PRODUCTOS_EDITAR");
        saved.setPermissionName("Editar productos");
        saved.setModule(Permission.Module.PRODUCTOS);
        saved.setDescription("Permite editar productos");
        saved.setStatus(Permission.Status.ACTIVO);

        PermissionResponseDTO response = new PermissionResponseDTO();
        response.setPermissionId(2L);
        response.setPermissionCode("PRODUCTOS_EDITAR");
        response.setPermissionName("Editar productos");
        response.setModule(Permission.Module.PRODUCTOS);
        response.setDescription("Permite editar productos");
        response.setStatus(Permission.Status.ACTIVO);

        when(mapper.toEntity(permissionDTO)).thenReturn(newPermission);
        when(repository.save(newPermission)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(response);

        PermissionResponseDTO result = service.createPermission(permissionDTO);

        assertNotNull(result);
        assertEquals(2L, result.getPermissionId());
        assertEquals("PRODUCTOS_EDITAR", result.getPermissionCode());
        assertEquals(Permission.Status.ACTIVO, result.getStatus());

        verify(mapper).toEntity(permissionDTO);
        verify(repository).save(newPermission);
        verify(mapper).toResponseDTO(saved);
    }

    @Test
    void updatePermission() {
        PermissionUpdateDTO updateDTO = new PermissionUpdateDTO();
        updateDTO.setPermissionName("Editar productos");
        updateDTO.setModule(Permission.Module.PRODUCTOS);
        updateDTO.setDescription("Permite editar productos");

        Permission updatedPermission = new Permission();
        updatedPermission.setPermissionId(2L);
        updatedPermission.setPermissionCode("PRODUCTOS_EDITAR");
        updatedPermission.setPermissionName("Editar productos");
        updatedPermission.setModule(Permission.Module.PRODUCTOS);
        updatedPermission.setDescription("Permite editar productos");
        updatedPermission.setStatus(Permission.Status.ACTIVO);

        PermissionResponseDTO response = new PermissionResponseDTO();
        response.setPermissionId(2L);
        response.setPermissionCode("PRODUCTOS_EDITAR");
        response.setPermissionName("Editar productos");
        response.setModule(Permission.Module.PRODUCTOS);
        response.setDescription("Permite editar productos");
        response.setStatus(Permission.Status.ACTIVO);

        when(repository.findById(2L)).thenReturn(Optional.of(permission));
        when(repository.save(permission)).thenReturn(updatedPermission);
        when(mapper.toResponseDTO(updatedPermission)).thenReturn(response);

        PermissionResponseDTO result = service.updatePermission(2L, updateDTO);

        assertNotNull(result);
        assertEquals(2L, result.getPermissionId());
        assertEquals("PRODUCTOS_EDITAR", result.getPermissionCode());
        assertEquals(Permission.Status.ACTIVO, result.getStatus());

        verify(repository).findById(2L);
        verify(mapper).updateEntity(updateDTO, permission);
        verify(repository).save(permission);
        verify(mapper).toResponseDTO(updatedPermission);
    }

    @Test
    void updatePermissionNotExists() {
        PermissionUpdateDTO updateDTO = new PermissionUpdateDTO();
        updateDTO.setPermissionName("Eliminar productos");
        updateDTO.setModule(Permission.Module.PRODUCTOS);

        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updatePermission(10L, updateDTO));

        assertEquals("El permiso 10 no existe", exception.getMessage());

        verify(repository).findById(10L);
        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusInactive() {
        Permission updatedPermission = new Permission();
        updatedPermission.setPermissionId(1L);
        updatedPermission.setPermissionCode("PRODUCTOS_CREAR");
        updatedPermission.setPermissionName("Crear productos");
        updatedPermission.setModule(Permission.Module.PRODUCTOS);
        updatedPermission.setDescription("Permite crear productos");
        updatedPermission.setStatus(Permission.Status.INACTIVO);

        PermissionResponseDTO response = new PermissionResponseDTO();
        response.setPermissionId(1L);
        response.setPermissionCode("PRODUCTOS_CREAR");
        response.setPermissionName("Crear productos");
        response.setModule(Permission.Module.PRODUCTOS);
        response.setDescription("Permite crear productos");
        response.setStatus(Permission.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(permission));
        when(repository.save(permission)).thenReturn(updatedPermission);
        when(mapper.toResponseDTO(updatedPermission)).thenReturn(response);

        PermissionResponseDTO result = service.changeStatus(1L, Permission.Status.INACTIVO);

        assertNotNull(result);
        assertEquals(1L, result.getPermissionId());
        assertEquals(Permission.Status.INACTIVO, permission.getStatus());
        assertEquals(Permission.Status.INACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(repository).save(permission);
        verify(mapper).toResponseDTO(updatedPermission);
    }

    @Test
    void changeStatusActive() {
        permission.setStatus(Permission.Status.INACTIVO);

        Permission updatedPermission = new Permission();
        updatedPermission.setPermissionId(1L);
        updatedPermission.setPermissionCode("PRODUCTOS_CREAR");
        updatedPermission.setPermissionName("Crear productos");
        updatedPermission.setModule(Permission.Module.PRODUCTOS);
        updatedPermission.setDescription("Permite crear productos");
        updatedPermission.setStatus(Permission.Status.ACTIVO);

        PermissionResponseDTO response = new PermissionResponseDTO();
        response.setPermissionId(1L);
        response.setPermissionCode("PRODUCTOS_CREAR");
        response.setPermissionName("Crear productos");
        response.setModule(Permission.Module.PRODUCTOS);
        response.setDescription("Permite crear productos");
        response.setStatus(Permission.Status.ACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(permission));
        when(repository.save(permission)).thenReturn(updatedPermission);
        when(mapper.toResponseDTO(updatedPermission)).thenReturn(response);

        PermissionResponseDTO result = service.changeStatus(1L, Permission.Status.ACTIVO);

        assertNotNull(result);
        assertEquals(1L, result.getPermissionId());
        assertEquals(Permission.Status.ACTIVO, permission.getStatus());
        assertEquals(Permission.Status.ACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(repository).save(permission);
        verify(mapper).toResponseDTO(updatedPermission);
    }

    @Test
    void changeStatusWhenAlreadyActive() {
        when(repository.findById(1L)).thenReturn(Optional.of(permission));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Permission.Status.ACTIVO));

        assertEquals("El permiso ya esta ACTIVO", exception.getMessage());

        verify(repository).findById(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWhenAlreadyInactive() {
        permission.setStatus(Permission.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(permission));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Permission.Status.INACTIVO));

        assertEquals("El permiso ya esta INACTIVO", exception.getMessage());

        verify(repository).findById(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWhenPermissionNotExist() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.changeStatus(10L, Permission.Status.INACTIVO));

        assertEquals("El permiso 10 no existe", exception.getMessage());

        verify(repository).findById(10L);
        verifyNoInteractions(mapper);
    }
}