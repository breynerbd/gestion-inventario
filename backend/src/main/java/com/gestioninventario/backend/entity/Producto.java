package com.gestioninventario.backend.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {

    public enum UnidadMedida {
        UNIDAD,
        CAJA,
        KILOGRAMO,
        GRAMO,
        LITRO,
        METRO,
        PAQUETE
    }

    public enum Estado {
        ACTIVO,
        INACTIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id_producto;

    @Column(name = "codigo_producto", nullable = false, unique = true, length = 20)
    private String codigo_producto;

    @Column(name = "nombre_producto", nullable = false, length = 120)
    private String nombre_producto;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", nullable = false, length = 15)
    private UnidadMedida unidad_medida;

    @Column(name = "precio_compra", nullable = false, precision = 12, scale = 2)
    private BigDecimal precio_compra;

    @Column(name = "precio_venta", nullable = false, precision = 12, scale = 2)
    private BigDecimal precio_venta;

    @Column(name = "stock_actual", nullable = false)
    private Integer stock_actual;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stock_minimo;

    @Column(name = "stock_maximo", nullable = false)
    private Integer stock_maximo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private Estado estado;

    public Producto() {
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
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