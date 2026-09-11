package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.application.dto.auth.LoginRequestDTO;
import com.gestioninventario.backend.application.dto.auth.LoginResponseDTO;
import com.gestioninventario.backend.application.dto.auth.RefreshTokenRequestDTO;
import com.gestioninventario.backend.application.dto.auth.RegisterRequestDTO;
import com.gestioninventario.backend.domain.entity.Role;
import com.gestioninventario.backend.domain.entity.User;
import com.gestioninventario.backend.domain.entity.Binnacle;
import com.gestioninventario.backend.domain.exception.InvalidCredentialsException;
import com.gestioninventario.backend.domain.exception.DuplicateResourceException;
import com.gestioninventario.backend.domain.exception.ResourceNotFoundException;
import com.gestioninventario.backend.domain.exception.BlockedUserException;
import com.gestioninventario.backend.domain.exception.InactiveUserException;
import com.gestioninventario.backend.infrastructure.persistence.repository.RoleRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.UserRepository;
import com.gestioninventario.backend.infrastructure.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int MAX_INTENTOS = 5;
    private static final String ROL_REGISTRO = "OPERADOR";
    private static final String TOKEN_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final BinnacleService binnacleService;

    private void validateExistingData(RegisterRequestDTO registroRequest) {
        if (userRepository.existsByEmail(registroRequest.getEmail())) {
            throw new DuplicateResourceException("El correo electrónico ya está registrado");
        }

        if (userRepository.existsByUsername(registroRequest.getUsername())) {
            throw new DuplicateResourceException("El nombre de usuario ya está registrado");
        }

        if (userRepository.existsByPhone(registroRequest.getPhone())) {
            throw new DuplicateResourceException("El teléfono ya está registrado");
        }
    }

    private void validateUserStatus(User user) {
        if (user.getStatus() == User.Status.BLOQUEADO) {

            throw new BlockedUserException("El usuario se encuentra bloqueado");
        }

        if (user.getStatus() == User.Status.INACTIVO) {

            throw new InactiveUserException("El usuario se encuentra INACTIVO");
        }
    }

    private void validateACTIVORole(Role rol) {
        if (rol.getStatus() == Role.Status.INACTIVO) {
            throw new IllegalArgumentException("No te puedes registrar en este momento");
        }
    }

    public LoginResponseDTO registerUser(RegisterRequestDTO registerRequest) {
        validateExistingData(registerRequest);

        Role role = roleRepository.findByRoleName(ROL_REGISTRO)
            .orElseThrow(() -> new ResourceNotFoundException("El rol " + ROL_REGISTRO + " no existe"));

        validateACTIVORole(role);

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

        return response;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest, String ipAddress) {

        User user = userRepository
            .findByUsername(loginRequest.getUsername()).orElse(null);

        if(user == null){
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

            if (attempts >= MAX_INTENTOS) {
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

        return response;
    }

    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO refreshRequest) {

        String refreshToken = refreshRequest.getRefreshToken();

        String email;

        try {
            email = jwtService.extractUsername(refreshToken);
        } catch (Exception e) {
            throw new IllegalArgumentException("Refresh token inválido");
        }

        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("El usuario no existe"));

        if (!jwtService.isRefreshTokenValid(
                refreshToken,
                user.getEmail())) {

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

        return response;
    }
}