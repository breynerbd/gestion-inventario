package com.inventorymanagement.backend.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.inventorymanagement.backend.domain.entity.Role;
import com.inventorymanagement.backend.domain.entity.User;

class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    private static final String SECRET_KEY = "fisfishfskjhfskjfghshfgskghsgkjhsjgjdhfgdhfgkjdfhgkjdfghkjdf";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET_KEY, 900000, 604800000);

        Role role = new Role();
        role.setRoleName("ADMIN");

        user = new User();
        user.setUserId(1L);
        user.setEmail("breyner@gmail.com");
        user.setRole(role);
    }

    @Test
    void generateAccessToken() {
        String token = jwtService.generateAccessToken(user);

        assertNotNull(token);
        assertEquals("breyner@gmail.com", jwtService.extractUsername(token));
        assertEquals("ACCESS", jwtService.extractType(token));
    }

    @Test
    void generateRefreshToken() {
        String token = jwtService.generateRefreshToken(user);

        assertNotNull(token);
        assertEquals("breyner@gmail.com", jwtService.extractUsername(token));
        assertEquals("REFRESH", jwtService.extractType(token));
    }

    @Test
    void accessTokenIsValid() {
        String token = jwtService.generateAccessToken(user);

        boolean result = jwtService.isTokenValid(token, "breyner@gmail.com");
        assertTrue(result);
    }

    @Test
    void accessTokenWithDifferentEmailIsInvalid() {
        String token = jwtService.generateAccessToken(user);

        boolean result = jwtService.isTokenValid(token, "omar@gmail.com");

        assertFalse(result);
    }

    @Test
    void refreshTokenIsValid() {
        String token = jwtService.generateRefreshToken(user);

        boolean result = jwtService.isRefreshTokenValid(token, "breyner@gmail.com");
        assertTrue(result);
    }

    @Test
    void refreshTokenWithDifferentEmailIsInvalid() {
        String token = jwtService.generateRefreshToken(user);

        boolean result = jwtService.isRefreshTokenValid(token, "omar@gmail.com");

        assertFalse(result);
    }

    @Test
    void refreshTokenIsNotAccessToken() {
        String token = jwtService.generateRefreshToken(user);

        boolean result = jwtService.isTokenValid(token, "breyner@gmail.com");

        assertFalse(result);
    }

    @Test
    void accessTokenIsNotRefreshToken() {
        String token = jwtService.generateAccessToken(user);

        boolean result = jwtService.isRefreshTokenValid(token, "breyner@gmail.com");

        assertFalse(result);
    }
}