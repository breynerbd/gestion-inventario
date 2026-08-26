package com.gestioninventario.backend.service;

import com.gestioninventario.backend.entity.Producto;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listarProductos() {
        return repository.findAll();
    }

    public Producto obtenerProducto(Long id_producto) {

        return repository.findById(id_producto)
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + id_producto + " no existe"));
    }

    public Producto crearProducto(Producto producto) {
        return repository.save(producto);
    }

    public Producto actualizarProducto(Long id_producto, Producto productoActualizado) {

        Producto producto = repository.findById(id_producto)
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + id_producto + " no existe"));

        producto.setCodigo_producto(productoActualizado.getCodigo_producto());
        producto.setNombre_producto(productoActualizado.getNombre_producto());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setCategoria(productoActualizado.getCategoria());
        producto.setProveedor(productoActualizado.getProveedor());
        producto.setUnidad_medida(productoActualizado.getUnidad_medida());
        producto.setPrecio_compra(productoActualizado.getPrecio_compra());
        producto.setPrecio_venta(productoActualizado.getPrecio_venta());
        producto.setStock_actual(productoActualizado.getStock_actual());
        producto.setStock_minimo(productoActualizado.getStock_minimo());
        producto.setStock_maximo(productoActualizado.getStock_maximo());
        producto.setEstado(productoActualizado.getEstado());

        return repository.save(producto);
    }

    public void eliminarProducto(Long id_producto) {

        Producto producto = repository.findById(id_producto)
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + id_producto + " no existe"));

        repository.delete(producto);
    }
}