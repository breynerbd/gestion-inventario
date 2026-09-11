package com.gestioninventario.backend.infrastructure.security;

import com.gestioninventario.backend.domain.entity.User;
import com.gestioninventario.backend.infrastructure.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor 
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = usuarioRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("El usuario con correo " + email + " no existe"));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .authorities(
                new SimpleGrantedAuthority(
                    "ROLE_" + user.getRole().getRoleName()
                )
            )
            .build();
    }
}