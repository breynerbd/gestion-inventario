package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.application.dto.producto.ProductoCreateDTO;
import com.gestioninventario.backend.application.dto.producto.ProductoResponseDTO;
import com.gestioninventario.backend.application.dto.producto.ProductoUpdateDTO;
import com.gestioninventario.backend.domain.entity.Categoria;
import com.gestioninventario.backend.domain.entity.Producto;
import com.gestioninventario.backend.domain.entity.Proveedor;
import com.gestioninventario.backend.domain.exception.EstadoSinCambiosException;
import com.gestioninventario.backend.domain.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.application.mapper.ProductoMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.CategoriaRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.ProductoRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.ProveedorRepository;

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

    public ProductoResponseDTO cambiarEstado(Long id_producto,Producto.Estado estado) {

    Producto producto = repository.findById(id_producto)
        .orElseThrow(() ->new RecursoNoEncontradoException("El producto " + id_producto + " no existe"));

    if (producto.getEstado() == estado) {
        String mensaje = switch (estado) {
            case ACTIVO -> "El producto ya esta activo";
            case INACTIVO -> "El producto ya esta inactivo";
        };

        throw new EstadoSinCambiosException(mensaje);
    }

    producto.setEstado(estado);

    Producto productoActualizado = repository.save(producto);

    return mapper.toResponseDTO(productoActualizado);
}
}