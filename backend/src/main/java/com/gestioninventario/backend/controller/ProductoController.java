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

import com.gestioninventario.backend.entity.Producto;
import com.gestioninventario.backend.service.ProductoService;

@Controller
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos(){
        return ResponseEntity.ok(service.listarProductos());
    }

    @GetMapping("/{id_producto}")
    public ResponseEntity<Producto> listarProductoPorId(@PathVariable("id_producto") Long id_producto){
        return ResponseEntity.ok(service.obtenerProducto(id_producto));
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearProducto(producto));
    }

    @PutMapping("/{id_producto}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable("id_producto") Long id_producto, @RequestBody Producto productoActualizado){
        return ResponseEntity.ok(service.actualizarProducto(id_producto, productoActualizado));
    }

    @DeleteMapping("/{id_producto}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable("id_producto") Long id_producto){
        service.eliminarProducto(id_producto);
        return ResponseEntity.noContent().build();
    }
}