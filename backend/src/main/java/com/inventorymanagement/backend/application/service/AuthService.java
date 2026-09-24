package com.inventorymanagement.backend.application.service;

import com.inventorymanagement.backend.application.dto.auth.LoginRequestDTO;
import com.inventorymanagement.backend.application.dto.auth.LoginResponseDTO;
import com.inventorymanagement.backend.application.dto.auth.RefreshTokenRequestDTO;
import com.inventorymanagement.backend.application.dto.auth.RegisterRequestDTO;
import com.inventorymanagement.backend.domain.entity.Role;
import com.inventorymanagement.backend.domain.entity.User;
import com.inventorymanagement.backend.domain.entity.Binnacle;
import com.inventorymanagement.backend.domain.exception.InvalidCredentialsException;
import com.inventorymanagement.backend.domain.exception.DuplicateResourceException;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.domain.exception.BlockedUserException;
import com.inventorymanagement.backend.domain.exception.InactiveUserException;
import com.inventorymanagement.backend.infrastructure.persistence.repository.RoleRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.UserRepository;
import com.inventorymanagement.backend.infrastructure.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int MAX_ATTEMPTS = 5;
    private static final String REGISTER_ROLE = "OPERADOR";
    private static final String TOKEN_TYPE = "Bearer";

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final BinnacleService binnacleService;

    private void validateExistingData(RegisterRequestDTO registerRequest) {
        LOGGER.debug("Vilidando datos existentes para el registro");
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException("El correo electrónico ya está registrado");
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new DuplicateResourceException("El nombre de usuario ya está registrado");
        }

        if (userRepository.existsByPhone(registerRequest.getPhone())) {
            throw new DuplicateResourceException("El teléfono ya está registrado");
        }
    }

    private void validateUserStatus(User user) {
        LOGGER.debug("Vilidando estado del usuario");
        if (user.getStatus() == User.Status.BLOQUEADO) {

            throw new BlockedUserException("El usuario se encuentra bloqueado");
        }

        if (user.getStatus() == User.Status.INACTIVO) {

            throw new InactiveUserException("El usuario se encuentra INACTIVO");
        }
    }

    private void validateActiveRole(Role role) {
        LOGGER.debug("Vilidando estado del rol");
        if (role.getStatus() == Role.Status.INACTIVO) {
            throw new IllegalArgumentException("No te puedes registrar en este momento");
        }
    }

    public LoginResponseDTO registerUser(RegisterRequestDTO registerRequest) {
        LOGGER.debug("Registrando al usuario: {}", registerRequest.getUsername());
        validateExistingData(registerRequest);

        Role role = roleRepository.findByRoleName(REGISTER_ROLE)
            .orElseThrow(() -> {
                LOGGER.warn("No se encontro el rol: {}", REGISTER_ROLE);
                return new ResourceNotFoundException("El rol " + REGISTER_ROLE + " no existe");}
            );

        validateActiveRole(role);

        User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstNames(registerRequest.getFirstNames());
        user.setLastNames(registerRequest.getLastNames());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setRole(role);
        user.setStatus(User.Status.ACTIVO);
        user.setFailedAttempts(0);

        User savedUser = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(savedUser);

        String refreshToken = jwtService.generateRefreshToken(savedUser);

        LoginResponseDTO response = new LoginResponseDTO();

        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setTokenType(TOKEN_TYPE);
        response.setUserId(savedUser.getUserId());
        response.setUsername(savedUser.getUsername());

        if (savedUser.getRole() != null) {
            response.setRoleName(savedUser.getRole().getRoleName());
        }

        LOGGER.info("Se registro el usuario {}: {}", savedUser.getUserId(), savedUser.getUsername());

        return response;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest, String ipAddress) {
        LOGGER.debug("Iniciando sesion para: {}", loginRequest.getUsername());

        User user = userRepository
            .findByUsername(loginRequest.getUsername()).orElse(null);

        if(user == null){
            LOGGER.warn("No se encontro el usuario: {}", loginRequest.getUsername());
            binnacleService.register(loginRequest.getUsername(), ipAddress, Binnacle.Result.FAILED);
            
            throw new InvalidCredentialsException("Credenciales Invalidas");
        }

        validateUserStatus(user);

        boolean correctPassword  = passwordEncoder.matches(loginRequest.getPassword(),user.getPassword());

        if (!correctPassword ) {

            int attempts =
                    user.getFailedAttempts() == null
                            ? 0
                            : user.getFailedAttempts();

            attempts++;

            user.setFailedAttempts(attempts);

            if (attempts >= MAX_ATTEMPTS) {
                user.setStatus(
                        User.Status.BLOQUEADO);
            }

            userRepository.save(user);

            binnacleService.register(user.getUsername(), ipAddress, Binnacle.Result.FAILED);

            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        userRepository.save(user);

        binnacleService.register(user.getUsername(), ipAddress, Binnacle.Result.SUCCESSFUL);

        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken =jwtService.generateRefreshToken(user);

        LoginResponseDTO response = new LoginResponseDTO();

        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setTokenType(TOKEN_TYPE);
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());

        if (user.getRole() != null) {
            response.setRoleName(
                    user.getRole().getRoleName());
        }

        LOGGER.info("Inicio de sesion exitoso para: {}", user.getUsername());

        return response;
    }

    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO refreshRequest) {
        LOGGER.debug("Renovando token");

        String refreshToken = refreshRequest.getRefreshToken();

        String email;

        try {
            email = jwtService.extractUsername(refreshToken);
        } catch (Exception e) {
            LOGGER.warn("El refresh token no es valido");
            throw new IllegalArgumentException("Refresh token inválido", e);
        }

        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> {
                LOGGER.warn("No se encontro el usuario para renovar el token");
                return new ResourceNotFoundException("El usuario no existe");}
            );

        if (!jwtService.isRefreshTokenValid(
                refreshToken,
                user.getEmail())) {

            LOGGER.warn("El refresh token es inválido o ha expirado");
            throw new IllegalArgumentException("El refresh token es inválido o ha expirado");
        }

        validateUserStatus(user);

        String newAccessToken = jwtService.generateAccessToken(user);

        String newRefreshToken = jwtService.generateRefreshToken(user);

        LoginResponseDTO response = new LoginResponseDTO();

        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setTokenType(TOKEN_TYPE);
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());

        if (user.getRole() != null) {

            response.setRoleName(user.getRole().getRoleName());
        }

        LOGGER.info("El token se renovo correctamente");

        return response;
    }
}