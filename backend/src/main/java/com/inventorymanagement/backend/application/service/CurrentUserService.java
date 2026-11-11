package com.inventorymanagement.backend.application.service;

import com.inventorymanagement.backend.domain.entity.User;
import com.inventorymanagement.backend.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationCredentialsNotFoundException(
                    "No hay un usuario autenticado");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new AuthenticationCredentialsNotFoundException(
                                "El usuario autenticado no existe"));
    }
}