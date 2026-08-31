package com.gestioninventario.backend.mapper;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.dto.producto.ProductoCreateDTO;
import com.gestioninventario.backend.dto.producto.ProductoResponseDTO;
import com.gestioninventario.backend.dto.producto.ProductoUpdateDTO;
import com.gestioninventario.backend.entity.Categoria;
import com.gestioninventario.backend.entity.Producto;
import com.gestioninventario.backend.entity.Proveedor;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoCreateDTO dto, Categoria categoria, Proveedor proveedor) {

        Producto producto = new Producto();

        producto.setCodigo_producto(dto.getCodigo_producto());
        producto.setNombre_producto(dto.getNombre_producto());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);
        producto.setUnidad_medida(dto.getUnidad_medida());
        producto.setPrecio_compra(dto.getPrecio_compra());
        producto.setPrecio_venta(dto.getPrecio_venta());
        producto.setStock_actual(0);
        producto.setStock_minimo(dto.getStock_minimo());
        producto.setStock_maximo(dto.getStock_maximo());
        producto.setEstado(Producto.Estado.ACTIVO);

        return producto;
    }

    public void updateEntity(ProductoUpdateDTO dto, Producto producto, Categoria categoria, Proveedor proveedor) {

        producto.setNombre_producto(dto.getNombre_producto());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);
        producto.setUnidad_medida(dto.getUnidad_medida());
        producto.setPrecio_compra(dto.getPrecio_compra());
        producto.setPrecio_venta(dto.getPrecio_venta());
        producto.setStock_minimo(dto.getStock_minimo());
        producto.setStock_maximo(dto.getStock_maximo());
    }

    public ProductoResponseDTO toResponseDTO(Producto producto) {

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

        return dto;
    }
}