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

import com.inventorymanagement.backend.application.dto.role.RoleCreateDTO;
import com.inventorymanagement.backend.application.dto.role.RoleResponseDTO;
import com.inventorymanagement.backend.application.dto.role.RoleUpdateDTO;
import com.inventorymanagement.backend.application.mapper.RoleMapper;
import com.inventorymanagement.backend.domain.entity.Role;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.infrastructure.persistence.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository repository;

    @Mock
    private RoleMapper mapper;

    @InjectMocks
    private RoleService service;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleId(1L);
        role.setRoleName("ADMINISTRADOR");
        role.setDescription("Rol de administrador");
        role.setStatus(Role.Status.ACTIVO);
    }

    @Test
    void findRoleById() {
        RoleResponseDTO response = new RoleResponseDTO();
        response.setRoleId(1L);
        response.setRoleName("ADMINISTRADOR");
        response.setDescription("Rol de administrador");
        response.setStatus(Role.Status.ACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(role));
        when(mapper.toResponseDTO(role)).thenReturn(response);

        RoleResponseDTO result = service.findRoleById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getRoleId());
        assertEquals("ADMINISTRADOR", result.getRoleName());
        assertEquals(Role.Status.ACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(mapper).toResponseDTO(role);
    }

    @Test
    void findRoleByIdReturnException() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findRoleById(10L));

        assertEquals("El rol 10 no existe", exception.getMessage());

        verify(repository).findById(10L);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAllRoles() {
        RoleResponseDTO response = new RoleResponseDTO();
        response.setRoleId(1L);
        response.setRoleName("ADMINISTRADOR");
        response.setDescription("Rol de administrador");
        response.setStatus(Role.Status.ACTIVO);

        when(repository.findAll()).thenReturn(List.of(role));
        when(mapper.toResponseDTO(role)).thenReturn(response);

        List<RoleResponseDTO> result = service.findAllRoles();

        assertEquals(1, result.size());
        assertEquals("ADMINISTRADOR", result.get(0).getRoleName());

        verify(repository).findAll();
        verify(mapper).toResponseDTO(role);
    }

    @Test
    void createRole() {
        RoleCreateDTO roleDTO = new RoleCreateDTO();
        roleDTO.setRoleName("SUPERVISOR");
        roleDTO.setDescription("Rol de supervisor");

        Role newRole = new Role();
        newRole.setRoleName("SUPERVISOR");
        newRole.setDescription("Rol de supervisor");

        Role savedRole = new Role();
        savedRole.setRoleId(2L);
        savedRole.setRoleName("SUPERVISOR");
        savedRole.setDescription("Rol de supervisor");
        savedRole.setStatus(Role.Status.ACTIVO);

        RoleResponseDTO response = new RoleResponseDTO();
        response.setRoleId(2L);
        response.setRoleName("SUPERVISOR");
        response.setDescription("Rol de supervisor");
        response.setStatus(Role.Status.ACTIVO);

        when(mapper.toEntity(roleDTO)).thenReturn(newRole);
        when(repository.save(newRole)).thenReturn(savedRole);
        when(mapper.toResponseDTO(savedRole)).thenReturn(response);

        RoleResponseDTO result = service.createRole(roleDTO);

        assertNotNull(result);
        assertEquals(2L, result.getRoleId());
        assertEquals("SUPERVISOR", result.getRoleName());
        assertEquals(Role.Status.ACTIVO, result.getStatus());

        verify(mapper).toEntity(roleDTO);
        verify(repository).save(newRole);
        verify(mapper).toResponseDTO(savedRole);
    }

    @Test
    void updateRole() {
        RoleUpdateDTO updateDTO = new RoleUpdateDTO();
        updateDTO.setRoleName("OPERADOR");
        updateDTO.setDescription("Rol de operador");

        Role updatedRole = new Role();
        updatedRole.setRoleId(1L);
        updatedRole.setRoleName("OPERADOR");
        updatedRole.setDescription("Rol de operador");
        updatedRole.setStatus(Role.Status.ACTIVO);

        RoleResponseDTO response = new RoleResponseDTO();
        response.setRoleId(1L);
        response.setRoleName("OPERADOR");
        response.setDescription("Rol de operador");
        response.setStatus(Role.Status.ACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(role));
        when(repository.save(role)).thenReturn(updatedRole);
        when(mapper.toResponseDTO(updatedRole)).thenReturn(response);

        RoleResponseDTO result = service.updateRole(1L, updateDTO);

        assertNotNull(result);
        assertEquals(1L, result.getRoleId());
        assertEquals("OPERADOR", result.getRoleName());
        assertEquals(Role.Status.ACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(mapper).updateEntity(updateDTO, role);
        verify(repository).save(role);
        verify(mapper).toResponseDTO(updatedRole);
    }

    @Test
    void updateRoleNotExists() {
        RoleUpdateDTO updateDTO = new RoleUpdateDTO();
        updateDTO.setRoleName("SUPERVISOR");
        updateDTO.setDescription("Rol de supervisor");

        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updateRole(10L, updateDTO));

        assertEquals("El rol 10 no existe", exception.getMessage());

        verify(repository).findById(10L);
        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusInactive() {
        Role updatedRole = new Role();
        updatedRole.setRoleId(1L);
        updatedRole.setRoleName("ADMINISTRADOR");
        updatedRole.setDescription("Rol de administrador");
        updatedRole.setStatus(Role.Status.INACTIVO);

        RoleResponseDTO response = new RoleResponseDTO();
        response.setRoleId(1L);
        response.setRoleName("ADMINISTRADOR");
        response.setDescription("Rol de administrador");
        response.setStatus(Role.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(role));
        when(repository.save(role)).thenReturn(updatedRole);
        when(mapper.toResponseDTO(updatedRole)).thenReturn(response);

        RoleResponseDTO result = service.changeStatus(1L, Role.Status.INACTIVO);

        assertNotNull(result);
        assertEquals(1L, result.getRoleId());
        assertEquals(Role.Status.INACTIVO, role.getStatus());
        assertEquals(Role.Status.INACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(repository).save(role);
        verify(mapper).toResponseDTO(updatedRole);
    }

    @Test
    void changeStatusActive() {
        role.setStatus(Role.Status.INACTIVO);

        Role updatedRole = new Role();
        updatedRole.setRoleId(1L);
        updatedRole.setRoleName("ADMINISTRADOR");
        updatedRole.setDescription("Rol de administrador");
        updatedRole.setStatus(Role.Status.ACTIVO);

        RoleResponseDTO response = new RoleResponseDTO();
        response.setRoleId(1L);
        response.setRoleName("ADMINISTRADOR");
        response.setDescription("Rol de administrador");
        response.setStatus(Role.Status.ACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(role));
        when(repository.save(role)).thenReturn(updatedRole);
        when(mapper.toResponseDTO(updatedRole)).thenReturn(response);

        RoleResponseDTO result = service.changeStatus(1L, Role.Status.ACTIVO);

        assertNotNull(result);
        assertEquals(1L, result.getRoleId());
        assertEquals(Role.Status.ACTIVO, role.getStatus());
        assertEquals(Role.Status.ACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(repository).save(role);
        verify(mapper).toResponseDTO(updatedRole);
    }

    @Test
    void changeStatusWhenAlreadyActive() {
        when(repository.findById(1L)).thenReturn(Optional.of(role));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Role.Status.ACTIVO));

        assertEquals("El rol ya esta ACTIVO", exception.getMessage());

        verify(repository).findById(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWhenAlreadyInactive() {
        role.setStatus(Role.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(role));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Role.Status.INACTIVO));

        assertEquals("El rol ya esta INACTIVO", exception.getMessage());

        verify(repository).findById(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWhenRoleNotExist() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.changeStatus(10L, Role.Status.INACTIVO));

        assertEquals("El rol 10 no existe", exception.getMessage());

        verify(repository).findById(10L);
        verifyNoInteractions(mapper);
    }
}