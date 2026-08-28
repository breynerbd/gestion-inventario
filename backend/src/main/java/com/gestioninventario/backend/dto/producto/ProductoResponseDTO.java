package com.gestioninventario.backend.dto.producto;

import java.math.BigDecimal;

import com.gestioninventario.backend.entity.Producto.Estado;
import com.gestioninventario.backend.entity.Producto.UnidadMedida;

public class ProductoResponseDTO {

    private Long id_producto;
    private String codigo_producto;
    private String nombre_producto;
    private String descripcion;

    private Long id_categoria;
    private Long id_proveedor;

    private UnidadMedida unidad_medida;
    private BigDecimal precio_compra;
    private BigDecimal precio_venta;

    private Integer stock_actual;
    private Integer stock_minimo;
    private Integer stock_maximo;

    private Estado estado;

    public ProductoResponseDTO() {
    }

    public Long getId_producto() {
        return id_producto;
    }

    public void setId_producto(Long id_producto) {
        this.id_producto = id_producto;
    }

    public String getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(String codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(Long id_categoria) {
        this.id_categoria = id_categoria;
    }

    public Long getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(Long id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public UnidadMedida getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(UnidadMedida unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public BigDecimal getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(BigDecimal precio_compra) {
        this.precio_compra = precio_compra;
    }

    public BigDecimal getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(BigDecimal precio_venta) {
        this.precio_venta = precio_venta;
    }

    public Integer getStock_actual() {
        return stock_actual;
    }

    public void setStock_actual(Integer stock_actual) {
        this.stock_actual = stock_actual;
    }

    public Integer getStock_minimo() {
        return stock_minimo;
    }

    public void setStock_minimo(Integer stock_minimo) {
        this.stock_minimo = stock_minimo;
    }

    public Integer getStock_maximo() {
        return stock_maximo;
    }

    public void setStock_maximo(Integer stock_maximo) {
        this.stock_maximo = stock_maximo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}