package com.gestioninventario.backend.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.gestioninventario.backend.application.dto.proveedor.ProveedorCreateDTO;
import com.gestioninventario.backend.application.dto.proveedor.ProveedorEstadoDTO;
import com.gestioninventario.backend.application.dto.proveedor.ProveedorResponseDTO;
import com.gestioninventario.backend.application.dto.proveedor.ProveedorUpdateDTO;
import com.gestioninventario.backend.application.service.ProveedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService service;

    public ProveedorController(ProveedorService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ProveedorResponseDTO>> listarProveedores(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(service.listarProveedores(pageable));
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

    @PatchMapping("/{id_proveedor}/estado")
    public ResponseEntity<ProveedorResponseDTO> cambiarEstado(@PathVariable("id_proveedor") Long id_proveedor, @Valid @RequestBody ProveedorEstadoDTO estadoDto) {
        return ResponseEntity.ok(service.cambiarEstado(id_proveedor,estadoDto.getEstado()));
    }
}