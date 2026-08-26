package com.gestioninventario.backend.entity;

import java.time.LocalDateTime;

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
@Table(name = "movimientos_stock")
public class MovimientoStock {

    public enum TipoMovimiento {
        ENTRADA,
        SALIDA,
        AJUSTE_POSITIVO,
        AJUSTE_NEGATIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long id_movimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private TipoMovimiento tipo_movimiento;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "documento_referencia", length = 30)
    private String documento_referencia;

    @Column(name = "motivo", length = 250)
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fecha_movimiento;

    public MovimientoStock() {
    }

    public Long getId_movimiento() {
        return id_movimiento;
    }

    public void setId_movimiento(Long id_movimiento) {
        this.id_movimiento = id_movimiento;
    }

    public TipoMovimiento getTipo_movimiento() {
        return tipo_movimiento;
    }

    public void setTipo_movimiento(TipoMovimiento tipo_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDocumento_referencia() {
        return documento_referencia;
    }

    public void setDocumento_referencia(String documento_referencia) {
        this.documento_referencia = documento_referencia;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha_movimiento() {
        return fecha_movimiento;
    }

    public void setFecha_movimiento(LocalDateTime fecha_movimiento) {
        this.fecha_movimiento = fecha_movimiento;
    }

}