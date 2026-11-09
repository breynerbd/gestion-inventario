package com.inventorymanagement.backend.infrastructure.security;

import com.inventorymanagement.backend.domain.entity.User;
import com.inventorymanagement.backend.infrastructure.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import com.inventorymanagement.backend.application.service.RolePermissionService;
import com.inventorymanagement.backend.domain.entity.Permission;

@RequiredArgsConstructor 
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository usuarioRepository;
    private final RolePermissionService rolePermissionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Buscando usuario para autenticacion");

        User user = usuarioRepository
            .findByEmail(email)
            .orElseThrow(() -> {
                LOGGER.warn("No se encontro el usuario para la autenticacion");
                return new UsernameNotFoundException("El usuario con correo " + email + " no existe");
            });

        var role = user.getRole();
        var authorities = new ArrayList<SimpleGrantedAuthority>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));

        for (var permission : rolePermissionService.findAllRolePermissions(role.getRoleId())) {
            if (permission.getStatus() == Permission.Status.ACTIVO) {
                authorities.add(
                        new SimpleGrantedAuthority("PERMISSION_" + permission.getPermissionCode())
                );
            }
        }

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
                .authorities(authorities)
            .build();
    }
}