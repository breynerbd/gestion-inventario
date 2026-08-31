package com.gestioninventario.backend.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestioninventario.backend.application.dto.permiso.PermisoCreateDTO;
import com.gestioninventario.backend.application.dto.permiso.PermisoResponseDTO;
import com.gestioninventario.backend.application.dto.permiso.PermisoUpdateDTO;
import com.gestioninventario.backend.application.service.PermisoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/permisos")
public class PermisoController {

    private final PermisoService service;

    public PermisoController(PermisoService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PermisoResponseDTO>> listarPermisos(){
        return ResponseEntity.ok(service.listarPermisos());
    }

    @GetMapping("/{id_permiso}")
    public ResponseEntity<PermisoResponseDTO> listarPermisoPorId(@PathVariable("id_permiso") Long id_permiso){
        return ResponseEntity.ok(service.obtenerPermiso(id_permiso));
    }

    @PostMapping
    public ResponseEntity<PermisoResponseDTO> crearPermiso(@Valid @RequestBody PermisoCreateDTO permiso){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearPermiso(permiso));
    }

    @PutMapping("/{id_permiso}")
    public ResponseEntity<PermisoResponseDTO> actualizarPermiso(@PathVariable("id_permiso") Long id_permiso, @Valid @RequestBody PermisoUpdateDTO permisoActualizado){
        return ResponseEntity.ok(service.actualizarPermiso(id_permiso, permisoActualizado));
    }

    @DeleteMapping("/{id_permiso}")
    public ResponseEntity<Void> eliminarPermiso(@PathVariable("id_permiso") Long id_permiso){
        service.eliminarPermiso(id_permiso);
        return ResponseEntity.noContent().build();
    }
}