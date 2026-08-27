package com.gestioninventario.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestioninventario.backend.entity.Categoria;
import com.gestioninventario.backend.service.CategoriaService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<List<Categoria>> listarCategorias() {
        return ResponseEntity.ok(service.listarCategorias());
    }

    @GetMapping("/{id_categoria}")
    public ResponseEntity<Categoria> listarCategoriaPorId(@PathVariable("id_categoria") Long id_categoria) {
        return ResponseEntity.ok(service.listarCategoriaPorId(id_categoria));
    }

    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearCategoria(categoria));
    }

    @PutMapping("/{id_categoria}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable("id_categoria") Long id_categoria, @RequestBody Categoria categoriaActualizada){
        return ResponseEntity.ok(service.actualizarCategoria(id_categoria, categoriaActualizada));
    }

    @DeleteMapping("/{id_categoria}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable("id_categoria") Long id_categoria){
        service.eliminarCategoria(id_categoria);
        return ResponseEntity.noContent().build();
    }
}