package com.gestioninventario.backend.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestioninventario.backend.application.dto.rolPermiso.RolPermisoDTO;
import com.gestioninventario.backend.application.dto.rolPermiso.RolPermisoResponseDTO;
import com.gestioninventario.backend.application.service.RolPermisoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles/{id_rol}/permisos")
public class RolPermisoController {

    private final RolPermisoService service;

    public RolPermisoController(RolPermisoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RolPermisoResponseDTO>> listarPermisosRol(@PathVariable("id_rol") Long id_rol) {
        return ResponseEntity.ok(service.listarPermisosRol(id_rol));
    }

    @PutMapping
    public ResponseEntity<List<RolPermisoResponseDTO>> asignarPermisos(@PathVariable("id_rol") Long id_rol,@Valid @RequestBody RolPermisoDTO dto) {
        return ResponseEntity.ok(service.asignarPermisos(id_rol, dto));
    }
}