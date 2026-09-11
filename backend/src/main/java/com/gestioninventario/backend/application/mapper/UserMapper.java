package com.gestioninventario.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.application.dto.user.UserCreateDTO;
import com.gestioninventario.backend.application.dto.user.UserResponseDTO;
import com.gestioninventario.backend.application.dto.user.UserUpdateDTO;
import com.gestioninventario.backend.domain.entity.Role;
import com.gestioninventario.backend.domain.entity.User;

@Component
public class UserMapper {

    public User toEntity(UserCreateDTO dto, Role role) {

        User user = new User();

        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFirstNames(dto.getFirstNames());
        user.setLastNames(dto.getLastNames());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setStatus(User.Status.ACTIVO);
        user.setFailedAttempts(0);
        user.setRole(role);

        return user;
    }

    public void updateEntity(UserUpdateDTO dto, User user, Role role) {

        user.setFirstNames(dto.getFirstNames());
        user.setLastNames(dto.getLastNames());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(role);
    }

    public UserResponseDTO toResponseDTO(User user) {

        UserResponseDTO dto = new UserResponseDTO();

        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFirstNames(user.getFirstNames());
        dto.setLastNames(user.getLastNames());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        if (user.getRole() != null) {
            dto.setRoleId(user.getRole().getRoleId());
            dto.setRoleName(user.getRole().getRoleName());
        }

        dto.setStatus(user.getStatus());
        dto.setFailedAttempts(user.getFailedAttempts());

        return dto;
    }
}