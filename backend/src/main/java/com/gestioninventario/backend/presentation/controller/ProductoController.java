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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestioninventario.backend.application.dto.producto.ProductoCreateDTO;
import com.gestioninventario.backend.application.dto.producto.ProductoEstadoDTO;
import com.gestioninventario.backend.application.dto.producto.ProductoResponseDTO;
import com.gestioninventario.backend.application.dto.producto.ProductoUpdateDTO;
import com.gestioninventario.backend.application.service.ProductoService;
import com.gestioninventario.backend.domain.entity.Producto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ProductoResponseDTO>> listarProductos(@RequestParam(required = false) String codigo,
            @RequestParam(required = false) String nombre,
            @RequestParam(name = "id_categoria", required = false) Long idCategoria,
            @RequestParam(name = "id_proveedor", required = false) Long idProveedor,
            @RequestParam(required = false) Producto.Estado estado,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(service.listarProductos(codigo, nombre, idCategoria, idProveedor, estado, pageable));
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

    @PatchMapping("/{id_producto}/estado")
    public ResponseEntity<ProductoResponseDTO> cambiarEstado(@PathVariable("id_producto") Long id_producto, @Valid @RequestBody ProductoEstadoDTO estadoDto) {
        return ResponseEntity.ok(service.cambiarEstado(id_producto,estadoDto.getEstado()));
    }
}