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
    RegisterRequestDTO register;
    private LoginRequestDTO login;
    private RefreshTokenRequestDTO refresh;
    private static final String IP_ADDRESS = "192.0.2.10";

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

        register = new RegisterRequestDTO();
        register.setUsername("omar497");
        register.setPassword("omar2021497.");
        register.setFirstNames("Omar Alexander");
        register.setLastNames("Benitez Cruz");
        register.setEmail("omara@gmail.com");
        register.setPhone("59317420");

        login = new LoginRequestDTO();
        login.setUsername("breynerbd");
        login.setPassword("breyner2007.");

        refresh = new RefreshTokenRequestDTO();
        refresh.setRefreshToken("refreshToken");
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

        verify(jwtService).generateAccessToken(savedUser);
    }

    @Test
    void emailExists() {
        when(userRepository.existsByEmail("omara@gmail.com")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> service.registerUser(register));

        assertEquals("El correo electrónico ya está registrado", exception.getMessage());
    }

    @Test
    void usernameExists() {
        when(userRepository.existsByUsername("omar497")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> service.registerUser(register));

        assertEquals("El nombre de usuario ya está registrado", exception.getMessage());
    }

    @Test
    void phoneExists() {
        when(userRepository.existsByPhone("59317420")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> service.registerUser(register));

        assertEquals("El teléfono ya está registrado", exception.getMessage());
    }

    @Test
    void roleNotFound() {
        when(roleRepository.findByRoleName("OPERADOR")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.registerUser(register));

        assertEquals("El rol OPERADOR no existe", exception.getMessage());
    }

    @Test
    void inactiveRole() {
        role.setStatus(Role.Status.INACTIVO);

        when(roleRepository.findByRoleName("OPERADOR")).thenReturn(Optional.of(role));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.registerUser(register));

        assertEquals("No te puedes registrar en este momento", exception.getMessage());
    }

    @Test
    void login() {
        when(userRepository.findByUsername("breynerbd")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("breyner2007.", "passwordEncoded")).thenReturn(true);
        when(jwtService.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        LoginResponseDTO result = service.login(login, IP_ADDRESS);

        assertNotNull(result);
        assertEquals("breynerbd", result.getUsername());
        assertEquals("Bearer", result.getTokenType());

        verify(userRepository).save(user);
        verify(binnacleService).register("breynerbd", IP_ADDRESS, Binnacle.Result.SUCCESSFUL);
    }

    @Test
    void userNotFound() {
        when(userRepository.findByUsername("breynerbd")).thenReturn(Optional.empty());

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> service.login(login, IP_ADDRESS));

        assertEquals("Credenciales Invalidas", exception.getMessage());

        verify(binnacleService).register("breynerbd", IP_ADDRESS, Binnacle.Result.FAILED);
    }

    @Test
    void blockUser() {
        user.setFailedAttempts(4);

        when(userRepository.findByUsername("breynerbd")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("breyner2007.", "passwordEncoded")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> service.login(login, IP_ADDRESS));

        assertEquals(5, user.getFailedAttempts());
        assertEquals(User.Status.BLOQUEADO, user.getStatus());

        verify(userRepository).save(user);
        verify(binnacleService).register("breynerbd", IP_ADDRESS, Binnacle.Result.FAILED);
    }

    @Test
    void blockedUser() {
        user.setStatus(User.Status.BLOQUEADO);

        when(userRepository.findByUsername("breynerbd")).thenReturn(Optional.of(user));

        BlockedUserException exception = assertThrows(BlockedUserException.class, () -> service.login(login, IP_ADDRESS));

        assertEquals("El usuario se encuentra bloqueado", exception.getMessage());
    }

    @Test
    void userInactive() {
        user.setStatus(User.Status.INACTIVO);

        when(userRepository.findByUsername("breynerbd")).thenReturn(Optional.of(user));

        InactiveUserException exception = assertThrows(InactiveUserException.class, () -> service.login(login, IP_ADDRESS));

        assertEquals("El usuario se encuentra INACTIVO", exception.getMessage());
    }

    @Test
    void refreshToken() {
        when(jwtService.extractUsername("refreshToken")).thenReturn("breyner@gmail.com");
        when(userRepository.findByEmail("breyner@gmail.com")).thenReturn(Optional.of(user));
        when(jwtService.isRefreshTokenValid("refreshToken", "breyner@gmail.com")).thenReturn(true);
        when(jwtService.generateAccessToken(user)).thenReturn("newAccessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("newRefreshToken");

        LoginResponseDTO result = service.refreshToken(refresh);

        assertNotNull(result);
        assertEquals("newAccessToken", result.getAccessToken());
        assertEquals("newRefreshToken", result.getRefreshToken());

        verify(jwtService).generateAccessToken(user);
        verify(jwtService).generateRefreshToken(user);
    }

    @Test
    void expiredRefreshToken() {
        when(jwtService.extractUsername("refreshToken")).thenReturn("breyner@gmail.com");
        when(userRepository.findByEmail("breyner@gmail.com")).thenReturn(Optional.of(user));
        when(jwtService.isRefreshTokenValid("refreshToken", "breyner@gmail.com")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.refreshToken(refresh));

        assertEquals("El refresh token es inválido o ha expirado", exception.getMessage());

        verify(jwtService, never()).generateAccessToken(user);
    }
}