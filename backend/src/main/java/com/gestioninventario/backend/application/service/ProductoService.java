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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    private void validarEntidadesActivas(Categoria categoria, Proveedor proveedor) {
        if (categoria.getEstado() == Categoria.Estado.INACTIVO) {
            throw new IllegalArgumentException("No se puede asociar el producto a una categoria inactiva");
        }

        if (proveedor.getEstado() == Proveedor.Estado.INACTIVO) {
            throw new IllegalArgumentException("No se puede asociar el producto a un proveedor inactivo");
        }
    }

    private void validarReglasProducto(BigDecimal precioCompra, BigDecimal precioVenta, Integer stockMinimo, Integer stockMaximo) {
        if (precioVenta.compareTo(precioCompra) < 0) {
            throw new IllegalArgumentException("El precio de venta no puede ser menor que el precio de compra");
        }

        if (stockMaximo != null && stockMaximo <= stockMinimo) {
            throw new IllegalArgumentException("El stock máximo debe ser mayor que el stock mínimo");
        }
    }

    private Pageable prepararPageable(Pageable pageable) {

        if (pageable.getSort().isUnsorted()) {
            return pageable;
        }

        Sort sort = Sort.unsorted();

        for (Sort.Order order : pageable.getSort()) {

            Sort nuevoOrden = JpaSort.unsafe(order.getDirection(), order.getProperty());

            sort = sort.and(nuevoOrden);
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    public Page<ProductoResponseDTO> listarProductos(String codigo, String nombre, Long idCategoria, Long idProveedor, Producto.Estado estado, Pageable pageable) {
        Specification<Producto> specification = Specification.unrestricted();
        if(codigo != null && !codigo.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) 
                -> criteriaBuilder.like(criteriaBuilder.lower(root.get("codigo_producto")), "%" + codigo.toLowerCase() + "%"));
        }

        if(nombre != null && !nombre.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) 
                -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre_producto")), "%" + nombre.toLowerCase() + "%"));
        }

        if (idCategoria != null) {
            specification = specification.and((root, query, criteriaBuilder) 
                -> criteriaBuilder.equal(root.get("categoria").get("id_categoria"),idCategoria));
        }

        if (idProveedor != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("proveedor").get("id_proveedor"),idProveedor));
        }

        if (estado != null) {
            specification = specification.and(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("estado"), estado));
        }

        Pageable pageableSeguro = prepararPageable(pageable);

        return repository.findAll(specification, pageableSeguro).map(mapper::toResponseDTO);
    }

    public ProductoResponseDTO obtenerProducto(Long id_producto) {
        Producto producto = repository.findById(id_producto)
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + id_producto + " no existe"));

        return mapper.toResponseDTO(producto);
    }

    public ProductoResponseDTO crearProducto(ProductoCreateDTO productoDto) {
        validarReglasProducto(
            productoDto.getPrecio_compra(),
            productoDto.getPrecio_venta(),
            productoDto.getStock_minimo(),
            productoDto.getStock_maximo()
        );

        Categoria categoria = categoriaRepository.findById(productoDto.getId_categoria()) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "La categoria " + productoDto.getId_categoria() + " no existe" ));

        Proveedor proveedor = proveedorRepository.findById(productoDto.getId_proveedor()) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "El proveedor " + productoDto.getId_proveedor() + " no existe" ));
        
        validarEntidadesActivas(categoria, proveedor);

        Producto producto = mapper.toEntity(productoDto, categoria, proveedor);
         
        Producto productoGuardado = repository.save(producto); 
         
        return mapper.toResponseDTO(productoGuardado);
    }

    public ProductoResponseDTO actualizarProducto(Long id_producto, ProductoUpdateDTO productoDto) {
        validarReglasProducto(
            productoDto.getPrecio_compra(),
            productoDto.getPrecio_venta(),
            productoDto.getStock_minimo(),
            productoDto.getStock_maximo()
        );

        Producto producto = repository.findById(id_producto) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "El producto " + id_producto + " no existe" )); 
            
        Categoria categoria = categoriaRepository.findById(productoDto.getId_categoria()) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "La categoria " + productoDto.getId_categoria() + " no existe" )); 
            
        Proveedor proveedor = proveedorRepository.findById(productoDto.getId_proveedor()) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "El proveedor " + productoDto.getId_proveedor() + " no existe" ));
        
        validarEntidadesActivas(categoria, proveedor);

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