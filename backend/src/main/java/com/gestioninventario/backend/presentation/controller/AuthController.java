package com.gestioninventario.backend.presentation.controller;

import com.gestioninventario.backend.application.dto.auth.LoginRequestDTO;
import com.gestioninventario.backend.application.dto.auth.LoginResponseDTO;
import com.gestioninventario.backend.application.dto.auth.RefreshTokenRequestDTO;
import com.gestioninventario.backend.application.dto.auth.RegistroRequestDTO;
import com.gestioninventario.backend.application.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/registro")
    public ResponseEntity<LoginResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarUsuario(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request, HttpServletRequest httpRequest) {
        String direccionIp = httpRequest.getRemoteAddr();
        return ResponseEntity.ok(service.login(request, direccionIp));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        return ResponseEntity.ok(service.refreshToken(request));
    }
}