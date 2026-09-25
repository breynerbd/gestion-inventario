package com.inventorymanagement.backend.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.inventorymanagement.backend.domain.entity.Role;
import com.inventorymanagement.backend.domain.entity.User;
import com.inventorymanagement.backend.infrastructure.persistence.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    private User user;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setRoleName("ADMIN");

        user = new User();
        user.setUserId(1L);
        user.setEmail("breyner@gmail.com");
        user.setPassword("password");
        user.setRole(role);
    }

    @Test
    void loadUserByUsername() {
        when(userRepository.findByEmail("breyner@gmail.com")).thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername("breyner@gmail.com");

        assertEquals("breyner@gmail.com", result.getUsername());
        assertEquals("password", result.getPassword());
        assertEquals("ROLE_ADMIN", result.getAuthorities().iterator().next().getAuthority());

        verify(userRepository).findByEmail("breyner@gmail.com");
    }

    @Test
    void loadUserByUsernameNotFound() {
        when(userRepository.findByEmail("omar@gmail.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("omar@gmail.com"));

        assertEquals("El usuario con correo omar@gmail.com no existe", exception.getMessage());

        verify(userRepository).findByEmail("omar@gmail.com");
    }
}