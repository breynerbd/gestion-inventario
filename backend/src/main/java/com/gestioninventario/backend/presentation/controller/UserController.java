package com.gestioninventario.backend.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestioninventario.backend.application.dto.user.UserCreateDTO;
import com.gestioninventario.backend.application.dto.user.UserStatusDTO;
import com.gestioninventario.backend.application.dto.user.UserResponseDTO;
import com.gestioninventario.backend.application.dto.user.UserUpdateDTO;
import com.gestioninventario.backend.application.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor 
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long userId){
        return ResponseEntity.ok(service.findUserById(userId));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO user){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(user));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long userId, @Valid @RequestBody UserUpdateDTO userDto){
        return ResponseEntity.ok(service.updatedUser(userId, userDto));
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserResponseDTO> changeStatus(@PathVariable Long userId, @Valid @RequestBody UserStatusDTO statusDTO) {
        return ResponseEntity.ok(service.changeStatus(userId, statusDTO.getStatus()));
    }
}