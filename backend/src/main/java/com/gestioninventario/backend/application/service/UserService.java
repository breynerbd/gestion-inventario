package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.domain.entity.Role;
import com.gestioninventario.backend.domain.entity.User;
import com.gestioninventario.backend.domain.exception.ResourceNotFoundException;
import com.gestioninventario.backend.application.dto.user.UserCreateDTO;
import com.gestioninventario.backend.application.dto.user.UserResponseDTO;
import com.gestioninventario.backend.application.dto.user.UserUpdateDTO;
import com.gestioninventario.backend.application.mapper.UserMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.RoleRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import com.gestioninventario.backend.domain.exception.StatusUnchangedException;
import com.gestioninventario.backend.domain.exception.DuplicateResourceException;

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

    private void validateActiveRole(Role role) {
        if (role.getStatus() == Role.Status.INACTIVO) {
            throw new IllegalArgumentException("No se puede crear un usuario con un rol INACTIVO");
        }
    }

    private void validateExistingData(UserCreateDTO userDto) {
        if (repository.existsByUsername(userDto.getUsername())) {
            throw new DuplicateResourceException("El nombre de usuario ya esta en uso");
        }

        if (repository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateResourceException("El correo electronico ya esta en uso");
        }

        if (repository.existsByPhone(userDto.getPhone())) {
            throw new DuplicateResourceException("El teléfono ya está registrado");
        }
    }

    public List<UserResponseDTO> findAllUsers() {

        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public UserResponseDTO findUserById(Long userId) {

        User user = repository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId + NOT_EXIST));

        return mapper.toResponseDTO(user);
    }

    public UserResponseDTO createUser(UserCreateDTO userDto) {
        validateExistingData(userDto);

        Role role = roleRepository.findById(userDto.getRoleId())
            .orElseThrow(() -> new ResourceNotFoundException("El rol " + userDto.getRoleId() + NOT_EXIST));
        
        validateActiveRole(role);

        User user = mapper.toEntity(userDto, role);

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User savedUser = repository.save(user);

        return mapper.toResponseDTO(savedUser);
    }

    public UserResponseDTO updatedUser(Long userId, UserUpdateDTO userDto) {
        User user = repository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId + NOT_EXIST));

        Role role = roleRepository.findById(userDto.getRoleId())
            .orElseThrow(() -> new ResourceNotFoundException("El rol " + userDto.getRoleId() + NOT_EXIST));
        
        validateActiveRole(role);

        mapper.updateEntity(userDto, user, role);

        User updatedUser = repository.save(user);

        return mapper.toResponseDTO(updatedUser);
    }

    public UserResponseDTO changeStatus(Long userId, User.Status status) {

        User user = repository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId + NOT_EXIST));

        if (user.getStatus() == status){
            String message = switch(status){
                case ACTIVO -> "El usuario ya esta ACTIVO";
                case INACTIVO -> "El usuario ya esta INACTIVO";
                case BLOQUEADO -> "El usuario ya esta bloqueado";
            };
            throw new StatusUnchangedException(message);
        }

        if (status == User.Status.ACTIVO) {
            validateActiveRole(user.getRole());
        }

        user.setStatus(status);

        User updatedUser = repository.save(user);

        return mapper.toResponseDTO(updatedUser);
    }
}