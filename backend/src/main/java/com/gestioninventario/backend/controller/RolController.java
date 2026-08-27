package com.gestioninventario.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gestioninventario.backend.entity.Rol;
import com.gestioninventario.backend.service.RolService;

@Controller
@RequestMapping("/api/roles")
public class RolController {

    private final RolService service;

    public RolController(RolService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Rol>> listarRoles(){
        return ResponseEntity.ok(service.listarRoles());
    }

    @GetMapping("/{id_rol}")
    public ResponseEntity<Rol> listarRolPorId(@PathVariable("id_rol") Long id_rol){
        return ResponseEntity.ok(service.obtenerRol(id_rol));
    }

    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearRol(rol));
    }

    @PutMapping("/{id_rol}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable("id_rol") Long id_rol, @RequestBody Rol rolActualizado){
        return ResponseEntity.ok(service.actualizarRol(id_rol, rolActualizado));
    }

    @DeleteMapping("/{id_rol}")
    public ResponseEntity<Void> eliminarRol(@PathVariable("id_rol") Long id_rol){
        service.eliminarRol(id_rol);
        return ResponseEntity.noContent().build();
    }
}