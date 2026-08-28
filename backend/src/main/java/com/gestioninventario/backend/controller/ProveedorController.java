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

import com.gestioninventario.backend.dto.proveedor.ProveedorCreateDTO;
import com.gestioninventario.backend.dto.proveedor.ProveedorResponseDTO;
import com.gestioninventario.backend.dto.proveedor.ProveedorUpdateDTO;
import com.gestioninventario.backend.service.ProveedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService service;

    public ProveedorController(ProveedorService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProveedorResponseDTO>> listarProveedores(){
        return ResponseEntity.ok(service.listarProveedores());
    }

    @GetMapping("/{id_proveedor}")
    public ResponseEntity<ProveedorResponseDTO> listarProveedorPorId(@PathVariable("id_proveedor") Long id_proveedor){
        return ResponseEntity.ok(service.obtenerProveedor(id_proveedor));
    }

    @PostMapping
    public ResponseEntity<ProveedorResponseDTO> crearProveedor(@Valid @RequestBody ProveedorCreateDTO proveedor){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearProveedor(proveedor));
    }

    @PutMapping("/{id_proveedor}")
    public ResponseEntity<ProveedorResponseDTO> actualizarProveedor(@PathVariable("id_proveedor") Long id_proveedor, @Valid @RequestBody ProveedorUpdateDTO proveedorActualizado){
        return ResponseEntity.ok(service.actualizarProveedor(id_proveedor, proveedorActualizado));
    }

    @DeleteMapping("/{id_proveedor}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable("id_proveedor") Long id_proveedor){
        service.eliminarProveedor(id_proveedor);
        return ResponseEntity.noContent().build();
    }
}