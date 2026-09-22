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
    private RoleResponseDTO response;
    private RoleCreateDTO roleDTO;
    private RoleUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleId(1L);
        role.setRoleName("ADMINISTRADOR");
        role.setDescription("Rol de administrador");
        role.setStatus(Role.Status.ACTIVO);

        response = new RoleResponseDTO();
        response.setRoleId(1L);
        response.setRoleName("ADMINISTRADOR");
        response.setDescription("Rol de administrador");
        response.setStatus(Role.Status.ACTIVO);

        roleDTO = new RoleCreateDTO();
        roleDTO.setRoleName("SUPERVISOR");
        roleDTO.setDescription("Rol de supervisor");

        updateDTO = new RoleUpdateDTO();
        updateDTO.setRoleName("OPERADOR");
        updateDTO.setDescription("Rol de operador");
    }

    @Test
    void findRoleById() {
        when(repository.findById(1L)).thenReturn(Optional.of(role));
        when(mapper.toResponseDTO(role)).thenReturn(response);

        RoleResponseDTO result = service.findRoleById(1L);

        assertNotNull(result);
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

        verifyNoInteractions(mapper);
    }

    @Test
    void findAllRoles() {
        when(repository.findAll()).thenReturn(List.of(role));
        when(mapper.toResponseDTO(role)).thenReturn(response);

        List<RoleResponseDTO> result = service.findAllRoles();

        assertEquals(1, result.size());
        assertEquals("ADMINISTRADOR", result.getFirst().getRoleName());

        verify(repository).findAll();
        verify(mapper).toResponseDTO(role);
    }

    @Test
    void createRole() {
        Role saved = new Role();
        saved.setRoleId(2L);
        saved.setRoleName("SUPERVISOR");
        saved.setDescription("Rol de supervisor");
        saved.setStatus(Role.Status.ACTIVO);

        response.setRoleId(2L);
        response.setRoleName("SUPERVISOR");
        response.setDescription("Rol de supervisor");

        when(mapper.toEntity(roleDTO)).thenReturn(role);
        when(repository.save(role)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(response);

        RoleResponseDTO result = service.createRole(roleDTO);

        assertNotNull(result);
        assertEquals(2L, result.getRoleId());
        assertEquals("SUPERVISOR", result.getRoleName());

        verify(mapper).toEntity(roleDTO);
        verify(repository).save(role);
        verify(mapper).toResponseDTO(saved);
    }

    @Test
    void updateRole() {
        when(repository.findById(1L)).thenReturn(Optional.of(role));
        when(repository.save(role)).thenReturn(role);
        when(mapper.toResponseDTO(role)).thenReturn(response);

        RoleResponseDTO result = service.updateRole(1L, updateDTO);

        assertNotNull(result);

        verify(mapper).updateEntity(updateDTO, role);
        verify(repository).save(role);
        verify(mapper).toResponseDTO(role);
    }

    @Test
    void updateRoleNotExists() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updateRole(10L, updateDTO));

        assertEquals("El rol 10 no existe", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusInactive() {
        response.setStatus(Role.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(role));
        when(repository.save(role)).thenReturn(role);
        when(mapper.toResponseDTO(role)).thenReturn(response);

        RoleResponseDTO result = service.changeStatus(1L, Role.Status.INACTIVO);

        assertNotNull(result);
        assertEquals(Role.Status.INACTIVO, role.getStatus());
        assertEquals(Role.Status.INACTIVO, result.getStatus());

        verify(repository).save(role);
    }

    @Test
    void changeStatusActive() {
        role.setStatus(Role.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(role));
        when(repository.save(role)).thenReturn(role);
        when(mapper.toResponseDTO(role)).thenReturn(response);

        RoleResponseDTO result = service.changeStatus(1L, Role.Status.ACTIVO);

        assertNotNull(result);
        assertEquals(Role.Status.ACTIVO, role.getStatus());
        assertEquals(Role.Status.ACTIVO, result.getStatus());

        verify(repository).save(role);
    }

    @Test
    void changeStatusWhenAlreadyActive() {
        when(repository.findById(1L)).thenReturn(Optional.of(role));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Role.Status.ACTIVO));

        assertEquals("El rol ya esta ACTIVO", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWhenAlreadyInactive() {
        role.setStatus(Role.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(role));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Role.Status.INACTIVO));

        assertEquals("El rol ya esta INACTIVO", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWhenRoleNotExist() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.changeStatus(10L, Role.Status.INACTIVO));

        assertEquals("El rol 10 no existe", exception.getMessage());

        verifyNoInteractions(mapper);
    }
}