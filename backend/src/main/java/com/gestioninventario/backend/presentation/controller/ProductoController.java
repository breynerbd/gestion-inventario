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

import com.gestioninventario.backend.application.dto.producto.ProductoCreateDTO;
import com.gestioninventario.backend.application.dto.producto.ProductoResponseDTO;
import com.gestioninventario.backend.application.dto.producto.ProductoUpdateDTO;
import com.gestioninventario.backend.application.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos(){
        return ResponseEntity.ok(service.listarProductos());
    }

    @GetMapping("/{id_producto}")
    public ResponseEntity<ProductoResponseDTO> listarProductoPorId(@PathVariable("id_producto") Long id_producto){
        return ResponseEntity.ok(service.obtenerProducto(id_producto));
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoCreateDTO producto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearProducto(producto));
    }

    @PutMapping("/{id_producto}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(@PathVariable("id_producto") Long id_producto, @Valid @RequestBody ProductoUpdateDTO productoActualizado){
        return ResponseEntity.ok(service.actualizarProducto(id_producto, productoActualizado));
    }

    @DeleteMapping("/{id_producto}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable("id_producto") Long id_producto){
        service.eliminarProducto(id_producto);
        return ResponseEntity.noContent().build();
    }
}