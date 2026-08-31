package com.gestioninventario.backend.service;

import com.gestioninventario.backend.dto.producto.ProductoCreateDTO;
import com.gestioninventario.backend.dto.producto.ProductoResponseDTO;
import com.gestioninventario.backend.dto.producto.ProductoUpdateDTO;
import com.gestioninventario.backend.entity.Categoria;
import com.gestioninventario.backend.entity.Producto;
import com.gestioninventario.backend.entity.Proveedor;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.mapper.ProductoMapper;
import com.gestioninventario.backend.repository.CategoriaRepository;
import com.gestioninventario.backend.repository.ProductoRepository;
import com.gestioninventario.backend.repository.ProveedorRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoMapper mapper;

    public ProductoService(ProductoRepository repository, CategoriaRepository categoriaRepository, ProveedorRepository proveedorRepository, ProductoMapper mapper) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
        this.mapper = mapper;
    }

    public List<ProductoResponseDTO> listarProductos() {
        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public ProductoResponseDTO obtenerProducto(Long id_producto) {
        Producto producto = repository.findById(id_producto)
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + id_producto + " no existe"));

        return mapper.toResponseDTO(producto);
    }

    public ProductoResponseDTO crearProducto(ProductoCreateDTO productoDto) {

        Categoria categoria = categoriaRepository.findById(productoDto.getId_categoria()) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "La categoria " + productoDto.getId_categoria() + " no existe" ));

        Proveedor proveedor = proveedorRepository.findById(productoDto.getId_proveedor()) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "El proveedor " + productoDto.getId_proveedor() + " no existe" ));

        Producto producto = mapper.toEntity(productoDto, categoria, proveedor);
         
        Producto productoGuardado = repository.save(producto); 
         
        return mapper.toResponseDTO(productoGuardado);
    }

    public ProductoResponseDTO actualizarProducto(Long id_producto, ProductoUpdateDTO productoDto) {

        Producto producto = repository.findById(id_producto) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "El producto " + id_producto + " no existe" )); 
            
        Categoria categoria = categoriaRepository.findById(productoDto.getId_categoria()) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "La categoria " + productoDto.getId_categoria() + " no existe" )); 
            
        Proveedor proveedor = proveedorRepository.findById(productoDto.getId_proveedor()) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "El proveedor " + productoDto.getId_proveedor() + " no existe" ));

        mapper.updateEntity(productoDto, producto, categoria, proveedor);
        
        Producto productoActualizado = repository.save(producto); 
        
        return mapper.toResponseDTO(productoActualizado);
    }

    public void eliminarProducto(Long id_producto) {

        Producto producto = repository.findById(id_producto)
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + id_producto + " no existe"));

        repository.delete(producto);
    }
}