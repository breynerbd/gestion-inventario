package com.inventorymanagement.backend.presentation.controller;

import com.inventorymanagement.backend.application.dto.auth.LoginRequestDTO;
import com.inventorymanagement.backend.application.dto.auth.LoginResponseDTO;
import com.inventorymanagement.backend.application.dto.auth.RefreshTokenRequestDTO;
import com.inventorymanagement.backend.application.dto.auth.RegisterRequestDTO;
import com.inventorymanagement.backend.application.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor 
public class AuthController {

    private final AuthService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        LOGGER.info("Solicitud para registrar un usuario");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request, HttpServletRequest httpRequest) {
        LOGGER.info("Solicitud para iniciar sesion");
        
        String ipAddress = httpRequest.getRemoteAddr();
        return ResponseEntity.ok(service.login(request, ipAddress));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        LOGGER.info("Solicitud para renovar token");
        return ResponseEntity.ok(service.refreshToken(request));
    }
}