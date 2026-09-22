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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inventorymanagement.backend.application.dto.user.UserCreateDTO;
import com.inventorymanagement.backend.application.dto.user.UserResponseDTO;
import com.inventorymanagement.backend.application.dto.user.UserUpdateDTO;
import com.inventorymanagement.backend.application.mapper.UserMapper;
import com.inventorymanagement.backend.domain.entity.Role;
import com.inventorymanagement.backend.domain.entity.User;
import com.inventorymanagement.backend.domain.exception.DuplicateResourceException;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.infrastructure.persistence.repository.RoleRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    private Role role;
    private User user;
    private UserResponseDTO response;
    private UserCreateDTO userDTO;
    private UserUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleId(1L);
        role.setRoleName("OPERADOR");
        role.setDescription("Rol de operador");
        role.setStatus(Role.Status.ACTIVO);

        user = new User();
        user.setUserId(1L);
        user.setFirstNames("Breyner");
        user.setLastNames("Benitez");
        user.setUsername("breynerbd");
        user.setEmail("breyner@gmail.com");
        user.setPhone("47823561");
        user.setStatus(User.Status.ACTIVO);
        user.setRole(role);

        response = new UserResponseDTO();
        response.setUserId(1L);
        response.setFirstNames("Breyner");
        response.setLastNames("Benitez");
        response.setUsername("breynerbd");
        response.setEmail("breyner@gmail.com");
        response.setPhone("47823561");
        response.setStatus(User.Status.ACTIVO);

        userDTO = new UserCreateDTO();
        userDTO.setFirstNames("Omar");
        userDTO.setLastNames("Benitez");
        userDTO.setUsername("omarbd");
        userDTO.setEmail("omar@gmail.com");
        userDTO.setPassword("omar2021497.");
        userDTO.setPhone("59317420");
        userDTO.setRoleId(1L);

        updateDTO = new UserUpdateDTO();
        updateDTO.setRoleId(1L);
    }

    @Test
    void findUserById() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toResponseDTO(user)).thenReturn(response);

        UserResponseDTO result = service.findUserById(1L);

        assertNotNull(result);
        assertEquals("breynerbd", result.getUsername());
        assertEquals("breyner@gmail.com", result.getEmail());

        verify(repository).findById(1L);
        verify(mapper).toResponseDTO(user);
    }

    @Test
    void findUserByIdReturnException() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findUserById(10L));

        assertEquals("El usuario 10 no existe", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void findAllUsers() {
        when(repository.findAll()).thenReturn(List.of(user));
        when(mapper.toResponseDTO(user)).thenReturn(response);

        List<UserResponseDTO> result = service.findAllUsers();

        assertEquals(1, result.size());
        assertEquals("breynerbd", result.get(0).getUsername());

        verify(repository).findAll();
        verify(mapper).toResponseDTO(user);
    }

    @Test
    void createUser() {
        User saved = new User();
        saved.setUserId(2L);
        saved.setFirstNames("Omar");
        saved.setLastNames("Benitez");
        saved.setUsername("omarbd");
        saved.setEmail("omar@gmail.com");
        saved.setPassword("encodedPassword");
        saved.setPhone("59317420");
        saved.setStatus(User.Status.ACTIVO);
        saved.setRole(role);

        response.setUserId(2L);
        response.setFirstNames("Omar");
        response.setLastNames("Benitez");
        response.setUsername("omarbd");
        response.setEmail("omar@gmail.com");
        response.setPhone("59317420");

        when(repository.existsByUsername("omarbd")).thenReturn(false);
        when(repository.existsByEmail("omar@gmail.com")).thenReturn(false);
        when(repository.existsByPhone("59317420")).thenReturn(false);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(mapper.toEntity(userDTO, role)).thenReturn(user);
        when(passwordEncoder.encode("omar2021497.")).thenReturn("encodedPassword");
        when(repository.save(user)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(response);

        UserResponseDTO result = service.createUser(userDTO);

        assertNotNull(result);
        assertEquals(2L, result.getUserId());
        assertEquals("omarbd", result.getUsername());
        assertEquals(User.Status.ACTIVO, result.getStatus());

        verify(mapper).toEntity(userDTO, role);
        verify(passwordEncoder).encode("omar2021497.");
        verify(repository).save(user);
    }

    @Test
    void createUserUsernameExists() {
        when(repository.existsByUsername("omarbd")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> service.createUser(userDTO));

        assertEquals("El nombre de usuario ya esta en uso", exception.getMessage());

        verifyNoInteractions(roleRepository);
        verifyNoInteractions(mapper);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void createUserEmailExists() {
        when(repository.existsByEmail("omar@gmail.com")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> service.createUser(userDTO));

        assertEquals("El correo electronico ya esta en uso", exception.getMessage());

        verifyNoInteractions(roleRepository);
        verifyNoInteractions(mapper);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void createUserPhoneExists() {
        when(repository.existsByPhone("59317420")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> service.createUser(userDTO));

        assertEquals("El teléfono ya está registrado", exception.getMessage());

        verifyNoInteractions(roleRepository);
        verifyNoInteractions(mapper);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void createUserInactiveRole() {
        role.setStatus(Role.Status.INACTIVO);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.createUser(userDTO));

        assertEquals("No se puede crear un usuario con un rol INACTIVO", exception.getMessage());

        verifyNoInteractions(mapper);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void updateUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(repository.save(user)).thenReturn(user);
        when(mapper.toResponseDTO(user)).thenReturn(response);

        UserResponseDTO result = service.updatedUser(1L, updateDTO);

        assertNotNull(result);

        verify(mapper).updateEntity(updateDTO, user, role);
        verify(repository).save(user);
        verify(mapper).toResponseDTO(user);
    }

    @Test
    void updateUserNotExists() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updatedUser(10L, updateDTO));

        assertEquals("El usuario 10 no existe", exception.getMessage());

        verifyNoInteractions(roleRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void updateUserRoleNotExist() {
        updateDTO.setRoleId(10L);

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updatedUser(1L, updateDTO));

        assertEquals("El rol 10 no existe", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusInactive() {
        response.setStatus(User.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);
        when(mapper.toResponseDTO(user)).thenReturn(response);

        UserResponseDTO result = service.changeStatus(1L, User.Status.INACTIVO);

        assertNotNull(result);
        assertEquals(User.Status.INACTIVO, user.getStatus());
        assertEquals(User.Status.INACTIVO, result.getStatus());

        verify(repository).save(user);
    }

    @Test
    void changeStatusActive() {
        user.setStatus(User.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);
        when(mapper.toResponseDTO(user)).thenReturn(response);

        UserResponseDTO result = service.changeStatus(1L, User.Status.ACTIVO);

        assertNotNull(result);
        assertEquals(User.Status.ACTIVO, user.getStatus());
        assertEquals(User.Status.ACTIVO, result.getStatus());

        verify(repository).save(user);
    }

    @Test
    void changeStatusBlocked() {
        response.setStatus(User.Status.BLOQUEADO);

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);
        when(mapper.toResponseDTO(user)).thenReturn(response);

        UserResponseDTO result = service.changeStatus(1L, User.Status.BLOQUEADO);

        assertNotNull(result);
        assertEquals(User.Status.BLOQUEADO, user.getStatus());
        assertEquals(User.Status.BLOQUEADO, result.getStatus());

        verify(repository).save(user);
    }

    @Test
    void changeStatusAlreadyActive() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, User.Status.ACTIVO));

        assertEquals("El usuario ya esta ACTIVO", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusAlreadyInactive() {
        user.setStatus(User.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, User.Status.INACTIVO));

        assertEquals("El usuario ya esta INACTIVO", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusAlreadyBlocked() {
        user.setStatus(User.Status.BLOQUEADO);

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, User.Status.BLOQUEADO));

        assertEquals("El usuario ya esta BLOQUEADO", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWhenUserNotExist() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.changeStatus(10L, User.Status.INACTIVO));

        assertEquals("El usuario 10 no existe", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWithInactiveRole() {
        user.setStatus(User.Status.INACTIVO);
        role.setStatus(Role.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.changeStatus(1L, User.Status.ACTIVO));

        assertEquals("No se puede crear un usuario con un rol INACTIVO", exception.getMessage());

        verifyNoInteractions(mapper);
    }
}