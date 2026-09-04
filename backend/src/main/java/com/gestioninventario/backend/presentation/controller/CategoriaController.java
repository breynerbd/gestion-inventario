package com.gestioninventario.backend.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestioninventario.backend.application.dto.categoria.CategoriaCreateDTO;
import com.gestioninventario.backend.application.dto.categoria.CategoriaEstadoDTO;
import com.gestioninventario.backend.application.dto.categoria.CategoriaResponseDTO;
import com.gestioninventario.backend.application.dto.categoria.CategoriaUpdateDTO;
import com.gestioninventario.backend.application.service.CategoriaService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        return ResponseEntity.ok(service.listarCategorias());
    }

    @GetMapping("/{id_categoria}")
    public ResponseEntity<CategoriaResponseDTO> listarCategoriaPorId(@PathVariable("id_categoria") Long id_categoria) {
        return ResponseEntity.ok(service.listarCategoriaPorId(id_categoria));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crearCategoria(@Valid @RequestBody CategoriaCreateDTO categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearCategoria(categoria));
    }

    @PutMapping("/{id_categoria}")
    public ResponseEntity<CategoriaResponseDTO> actualizarCategoria(@PathVariable("id_categoria") Long id_categoria, @Valid @RequestBody CategoriaUpdateDTO categoriaActualizada){
        return ResponseEntity.ok(service.actualizarCategoria(id_categoria, categoriaActualizada));
    }

    @PatchMapping("/{id_categoria}/estado")
    public ResponseEntity<CategoriaResponseDTO> cambiarEstado(@PathVariable("id_categoria") Long id_categoria, @Valid @RequestBody CategoriaEstadoDTO estadoDto) {
        return ResponseEntity.ok(service.cambiarEstado(id_categoria,estadoDto.getEstado()));
    }
}