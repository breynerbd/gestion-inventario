package com.gestioninventario.backend.controller;

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

import com.gestioninventario.backend.dto.rol.RolCreateDTO;
import com.gestioninventario.backend.dto.rol.RolResponseDTO;
import com.gestioninventario.backend.dto.rol.RolUpdateDTO;
import com.gestioninventario.backend.service.RolService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService service;

    public RolController(RolService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RolResponseDTO>> listarRoles(){
        return ResponseEntity.ok(service.listarRoles());
    }

    @GetMapping("/{id_rol}")
    public ResponseEntity<RolResponseDTO> listarRolPorId(@PathVariable("id_rol") Long id_rol){
        return ResponseEntity.ok(service.obtenerRol(id_rol));
    }

    @PostMapping
    public ResponseEntity<RolResponseDTO> crearRol(@Valid @RequestBody RolCreateDTO rol){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearRol(rol));
    }

    @PutMapping("/{id_rol}")
    public ResponseEntity<RolResponseDTO> actualizarRol(@PathVariable("id_rol") Long id_rol, @Valid @RequestBody RolUpdateDTO rolActualizado){
        return ResponseEntity.ok(service.actualizarRol(id_rol, rolActualizado));
    }

    @DeleteMapping("/{id_rol}")
    public ResponseEntity<Void> eliminarRol(@PathVariable("id_rol") Long id_rol){
        service.eliminarRol(id_rol);
        return ResponseEntity.noContent().build();
    }
}