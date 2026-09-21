package com.inventorymanagement.backend.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inventorymanagement.backend.application.dto.auth.LoginRequestDTO;
import com.inventorymanagement.backend.application.dto.auth.LoginResponseDTO;
import com.inventorymanagement.backend.application.dto.auth.RefreshTokenRequestDTO;
import com.inventorymanagement.backend.application.dto.auth.RegisterRequestDTO;
import com.inventorymanagement.backend.domain.entity.Binnacle;
import com.inventorymanagement.backend.domain.entity.Role;
import com.inventorymanagement.backend.domain.entity.User;
import com.inventorymanagement.backend.domain.exception.BlockedUserException;
import com.inventorymanagement.backend.domain.exception.DuplicateResourceException;
import com.inventorymanagement.backend.domain.exception.InactiveUserException;
import com.inventorymanagement.backend.domain.exception.InvalidCredentialsException;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.infrastructure.persistence.repository.RoleRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.UserRepository;
import com.inventorymanagement.backend.infrastructure.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private BinnacleService binnacleService;

    @InjectMocks
    private AuthService service;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleId(1L);
        role.setRoleName("OPERADOR");
        role.setStatus(Role.Status.ACTIVO);

        user = new User();
        user.setUserId(1L);
        user.setUsername("breynerbd");
        user.setFirstNames("Breyner Alexander");
        user.setLastNames("Benitez Diaz");
        user.setEmail("breyner@gmail.com");
        user.setPhone("47823561");
        user.setPassword("passwordEncoded");
        user.setRole(role);
        user.setStatus(User.Status.ACTIVO);
        user.setFailedAttempts(0);
    }

    @Test
    void registerUser() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("omar497");
        dto.setPassword("omar2021497.");
        dto.setFirstNames("Omar Alexander");
        dto.setLastNames("Benitez Cruz");
        dto.setEmail("omara@gmail.com");
        dto.setPhone("59317420");

        User savedUser = new User();
        savedUser.setUserId(2L);
        savedUser.setUsername("omar497");
        savedUser.setEmail("omara@gmail.com");
        savedUser.setPhone("59317420");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(role);
        savedUser.setStatus(User.Status.ACTIVO);
        savedUser.setFailedAttempts(0);

        when(userRepository.existsByEmail("omara@gmail.com")).thenReturn(false);
        when(userRepository.existsByUsername("omar497")).thenReturn(false);
        when(userRepository.existsByPhone("59317420")).thenReturn(false);
        when(roleRepository.findByRoleName("OPERADOR")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("omar2021497.")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateAccessToken(savedUser)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(savedUser)).thenReturn("refreshToken");

        LoginResponseDTO result = service.registerUser(dto);

        assertNotNull(result);
        assertEquals("omar497", result.getUsername());
        assertEquals(2L, result.getUserId());

        verify(userRepository).save(any(User.class));
        verify(jwtService).generateAccessToken(savedUser);
    }

    @Test
    void emailExists() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setEmail("breyner@gmail.com");

        when(userRepository.existsByEmail("breyner@gmail.com")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> service.registerUser(dto));

        assertEquals("El correo electrónico ya está registrado", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void usernameExists() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("omar497");

        when(userRepository.existsByUsername("omar497")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> service.registerUser(dto));

        assertEquals("El nombre de usuario ya está registrado", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void phoneExists() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setPhone("42681953");

        when(userRepository.existsByPhone("42681953")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> service.registerUser(dto));

        assertEquals("El teléfono ya está registrado", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void roleNotFound() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("breynerbd");
        dto.setEmail("breyner@gmail.com");
        dto.setPhone("51742683");
        
        when(roleRepository.findByRoleName("OPERADOR")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.registerUser(dto));

        assertEquals("El rol OPERADOR no existe", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void inactiveRole() {
        role.setStatus(Role.Status.INACTIVO);

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("fernando21");
        dto.setEmail("fernando@gmail.com");
        dto.setPhone("38264915");

        when(roleRepository.findByRoleName("OPERADOR")).thenReturn(Optional.of(role));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.registerUser(dto));

        assertEquals("No te puedes registrar en este momento", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("breynerbd");
        dto.setPassword("breyner2007.");

        when(userRepository.findByUsername("breynerbd")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("breyner2007.", "passwordEncoded")).thenReturn(true);
        when(jwtService.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        LoginResponseDTO result = service.login(dto, "192.168.1.24");

        assertNotNull(result);
        assertEquals("breynerbd", result.getUsername());
        assertEquals("Bearer", result.getTokenType());

        verify(userRepository).save(user);
        verify(binnacleService).register("breynerbd", "192.168.1.24", Binnacle.Result.SUCCESSFUL);
    }

    @Test
    void userNotFound() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("breynerbd");
        dto.setPassword("Password123");

        when(userRepository.findByUsername("breynerbd")).thenReturn(Optional.empty());

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> service.login(dto, "192.168.1.35"));

        assertEquals("Credenciales Invalidas", exception.getMessage());

        verify(binnacleService).register("breynerbd", "192.168.1.35", Binnacle.Result.FAILED);
    }

    @Test
    void blockUser() {
        user.setFailedAttempts(4);

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("breynerbd");
        dto.setPassword("breyner2007.");

        when(userRepository.findByUsername("breynerbd")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("breyner2007.", "passwordEncoded")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> service.login(dto, "192.168.1.58"));

        assertEquals(5, user.getFailedAttempts());
        assertEquals(User.Status.BLOQUEADO, user.getStatus());

        verify(userRepository).save(user);
        verify(binnacleService).register("breynerbd", "192.168.1.58", Binnacle.Result.FAILED);
    }

    @Test
    void blockedUser() {
        user.setStatus(User.Status.BLOQUEADO);

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("breynerbd");

        when(userRepository.findByUsername("breynerbd")).thenReturn(Optional.of(user));

        BlockedUserException exception = assertThrows(BlockedUserException.class, () -> service.login(dto, "192.168.1.63"));

        assertEquals("El usuario se encuentra bloqueado", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void userInactive() {
        user.setStatus(User.Status.INACTIVO);

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("breynerbd");

        when(userRepository.findByUsername("breynerbd")).thenReturn(Optional.of(user));

        InactiveUserException exception = assertThrows(InactiveUserException.class, () -> service.login(dto, "192.168.1.76"));

        assertEquals("El usuario se encuentra INACTIVO", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void refreshToken() {
        RefreshTokenRequestDTO dto = new RefreshTokenRequestDTO();
        dto.setRefreshToken("refreshToken");

        when(jwtService.extractUsername("refreshToken")).thenReturn("breyner@gmail.com");
        when(userRepository.findByEmail("breyner@gmail.com")).thenReturn(Optional.of(user));
        when(jwtService.isRefreshTokenValid("refreshToken", "breyner@gmail.com")).thenReturn(true);
        when(jwtService.generateAccessToken(user)).thenReturn("newAccessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("newRefreshToken");

        LoginResponseDTO result = service.refreshToken(dto);

        assertNotNull(result);
        assertEquals("newAccessToken", result.getAccessToken());
        assertEquals("newRefreshToken", result.getRefreshToken());

        verify(jwtService).generateAccessToken(user);
        verify(jwtService).generateRefreshToken(user);
    }

    @Test
    void expiredRefreshToken() {
        RefreshTokenRequestDTO dto = new RefreshTokenRequestDTO();
        dto.setRefreshToken("refreshToken");

        when(jwtService.extractUsername("refreshToken")).thenReturn("breyner@gmail.com");
        when(userRepository.findByEmail("breyner@gmail.com")).thenReturn(Optional.of(user));
        when(jwtService.isRefreshTokenValid("refreshToken", "breyner@gmail.com")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.refreshToken(dto));

        assertEquals("El refresh token es inválido o ha expirado", exception.getMessage());

        verify(jwtService, never()).generateAccessToken(user);
    }
}