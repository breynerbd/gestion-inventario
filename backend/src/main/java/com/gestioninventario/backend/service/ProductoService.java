package com.gestioninventario.backend.service;

import com.gestioninventario.backend.dto.producto.ProductoCreateDTO;
import com.gestioninventario.backend.dto.producto.ProductoResponseDTO;
import com.gestioninventario.backend.dto.producto.ProductoUpdateDTO;
import com.gestioninventario.backend.entity.Categoria;
import com.gestioninventario.backend.entity.Producto;
import com.gestioninventario.backend.entity.Proveedor;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
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

    public ProductoService(ProductoRepository repository, CategoriaRepository categoriaRepository, ProveedorRepository proveedorRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public List<ProductoResponseDTO> listarProductos() {
        return repository.findAll().stream().map(this::productoResponse).toList();
    }

    public ProductoResponseDTO obtenerProducto(Long id_producto) {
        Producto producto = repository.findById(id_producto)
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + id_producto + " no existe"));

        return productoResponse(producto);
    }

    public ProductoResponseDTO crearProducto(ProductoCreateDTO productoDto) {

        Categoria categoria = categoriaRepository.findById(productoDto.getId_categoria()) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "La categoria " + productoDto.getId_categoria() + " no existe" ));

        Proveedor proveedor = proveedorRepository.findById(productoDto.getId_proveedor()) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "El proveedor " + productoDto.getId_proveedor() + " no existe" ));

        Producto producto = new Producto(); 
        
        producto.setCodigo_producto(productoDto.getCodigo_producto()); 
        producto.setNombre_producto(productoDto.getNombre_producto()); 
        producto.setDescripcion(productoDto.getDescripcion()); 
        producto.setCategoria(categoria); 
        producto.setProveedor(proveedor); 
        producto.setUnidad_medida(productoDto.getUnidad_medida()); 
        producto.setPrecio_compra(productoDto.getPrecio_compra());
         producto.setPrecio_venta(productoDto.getPrecio_venta()); 
         producto.setStock_actual(0); 
         producto.setStock_minimo(productoDto.getStock_minimo()); 
         producto.setStock_maximo(productoDto.getStock_maximo()); 
         producto.setEstado(Producto.Estado.ACTIVO); 
         
         Producto productoGuardado = repository.save(producto); 
         
         return productoResponse(productoGuardado);
    }

    public ProductoResponseDTO actualizarProducto(Long id_producto, ProductoUpdateDTO productoDto) {

        Producto producto = repository.findById(id_producto) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "El producto " + id_producto + " no existe" )); 
            
        Categoria categoria = categoriaRepository.findById(productoDto.getId_categoria()) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "La categoria " + productoDto.getId_categoria() + " no existe" )); 
            
        Proveedor proveedor = proveedorRepository.findById(productoDto.getId_proveedor()) 
            .orElseThrow(() -> new RecursoNoEncontradoException( "El proveedor " + productoDto.getId_proveedor() + " no existe" ));

        producto.setNombre_producto(productoDto.getNombre_producto()); 
        producto.setDescripcion(productoDto.getDescripcion()); 
        producto.setCategoria(categoria); 
        producto.setProveedor(proveedor); 
        producto.setUnidad_medida(productoDto.getUnidad_medida()); 
        producto.setPrecio_compra(productoDto.getPrecio_compra()); 
        producto.setPrecio_venta(productoDto.getPrecio_venta()); 
        producto.setStock_minimo(productoDto.getStock_minimo()); 
        producto.setStock_maximo(productoDto.getStock_maximo()); 
        
        Producto productoActualizado = repository.save(producto); 
        
        return productoResponse(productoActualizado);
    }

    public void eliminarProducto(Long id_producto) {

        Producto producto = repository.findById(id_producto)
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + id_producto + " no existe"));

        repository.delete(producto);
    }

    private ProductoResponseDTO productoResponse(Producto producto) { 
        ProductoResponseDTO dto = new ProductoResponseDTO(); 
        
        dto.setId_producto(producto.getId_producto()); 
        dto.setCodigo_producto(producto.getCodigo_producto()); 
        dto.setNombre_producto(producto.getNombre_producto()); 
        dto.setDescripcion(producto.getDescripcion());
        dto.setId_categoria(producto.getCategoria().getId_categoria()); 
        dto.setId_proveedor(producto.getProveedor().getId_proveedor()); 
        dto.setUnidad_medida(producto.getUnidad_medida()); 
        dto.setPrecio_compra(producto.getPrecio_compra()); 
        dto.setPrecio_venta(producto.getPrecio_venta()); 
        dto.setStock_actual(producto.getStock_actual()); 
        dto.setStock_minimo(producto.getStock_minimo()); 
        dto.setStock_maximo(producto.getStock_maximo()); 
        dto.setEstado(producto.getEstado()); 
         
        return dto; }
}