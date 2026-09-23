package com.inventorymanagement.backend.application.service;

import com.inventorymanagement.backend.domain.entity.Role;
import com.inventorymanagement.backend.domain.entity.User;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.application.dto.user.UserCreateDTO;
import com.inventorymanagement.backend.application.dto.user.UserResponseDTO;
import com.inventorymanagement.backend.application.dto.user.UserUpdateDTO;
import com.inventorymanagement.backend.application.mapper.UserMapper;
import com.inventorymanagement.backend.infrastructure.persistence.repository.RoleRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.domain.exception.DuplicateResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor 
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private static final String USER_NOT_FOUND = "El usuario ";
    private static final String NOT_EXIST = " no existe";
    private static final String LOGGER_NOT_FOUND = "No se encontro el usuario: {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private void validateActiveRole(Role role) {
        LOGGER.debug("Validando que el rol este activo");

        if (role.getStatus() == Role.Status.INACTIVO) {
            throw new IllegalArgumentException("No se puede crear un usuario con un rol INACTIVO");
        }
    }

    private void validateExistingData(UserCreateDTO userDto) {
        LOGGER.debug("Validando datos del usuario");

        if (repository.existsByUsername(userDto.getUsername())) {
            LOGGER.warn("El nombre de usuario {} ya esta registrado", userDto.getUsername());
            throw new DuplicateResourceException("El nombre de usuario ya esta en uso");
        }

        if (repository.existsByEmail(userDto.getEmail())) {
            LOGGER.warn("El correo ya esta registrado");
            throw new DuplicateResourceException("El correo electronico ya esta en uso");
        }

        if (repository.existsByPhone(userDto.getPhone())) {
            LOGGER.warn("El telefono ya esta registrado");
            throw new DuplicateResourceException("El teléfono ya está registrado");
        }
    }

    public List<UserResponseDTO> findAllUsers() {
        LOGGER.debug("Obteniendo datos de usuarios existentes");

        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public UserResponseDTO findUserById(Long userId) {
        LOGGER.debug("Buscando usuario: {}", userId);

        User user = repository.findById(userId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, userId);
                return new ResourceNotFoundException(USER_NOT_FOUND + userId + NOT_EXIST);});

        return mapper.toResponseDTO(user);
    }

    public UserResponseDTO createUser(UserCreateDTO userDto) {
        LOGGER.debug("Creando usuario: {}", userDto.getUsername());

        validateExistingData(userDto);

        Role role = roleRepository.findById(userDto.getRoleId())
            .orElseThrow(() -> {
                LOGGER.warn("No se encontro el rol: {}", userDto.getRoleId());
                return new ResourceNotFoundException("El rol " + userDto.getRoleId() + NOT_EXIST);
            });
            
        validateActiveRole(role);

        User user = mapper.toEntity(userDto, role);

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User savedUser = repository.save(user);

        LOGGER.info("Se creo el usuario con id: {}", savedUser.getUserId());

        return mapper.toResponseDTO(savedUser);
    }

    public UserResponseDTO updatedUser(Long userId, UserUpdateDTO userDto) {
        LOGGER.debug("Actualizando usuario: {}", userId);

        User user = repository.findById(userId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, userId);
                return new ResourceNotFoundException(USER_NOT_FOUND + userId + NOT_EXIST);
            });

        Role role = roleRepository.findById(userDto.getRoleId())
            .orElseThrow(() -> {
                LOGGER.warn("No se encontro el rol: {}", userDto.getRoleId());
                return new ResourceNotFoundException("El rol " + userDto.getRoleId() + NOT_EXIST);
            });

        validateActiveRole(role);

        mapper.updateEntity(userDto, user, role);

        User updatedUser = repository.save(user);

        LOGGER.info("El usuario {} se ha actualizado", userId);

        return mapper.toResponseDTO(updatedUser);
    }

    public UserResponseDTO changeStatus(Long userId, User.Status status) {
        LOGGER.debug("Cambiando estado del usuario {} a {}", userId, status);

        User user = repository.findById(userId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, userId);
                return new ResourceNotFoundException(USER_NOT_FOUND + userId + NOT_EXIST);
            });

        if (user.getStatus() == status){
            String message = switch(status){
                case ACTIVO -> "El usuario ya esta ACTIVO";
                case INACTIVO -> "El usuario ya esta INACTIVO";
                case BLOQUEADO -> "El usuario ya esta BLOQUEADO";
            };
            throw new StatusUnchangedException(message);
        }

        if (status == User.Status.ACTIVO) {
            validateActiveRole(user.getRole());
        }

        user.setStatus(status);

        User updatedUser = repository.save(user);

        LOGGER.info("El estado del usuario {} se ha cambiado a {}", userId, status);

        return mapper.toResponseDTO(updatedUser);
    }
}