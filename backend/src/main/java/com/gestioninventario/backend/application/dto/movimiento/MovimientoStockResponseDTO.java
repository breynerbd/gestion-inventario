package com.gestioninventario.backend.application.dto.movimiento;

import java.time.LocalDateTime;

import com.gestioninventario.backend.domain.entity.MovimientoStock.TipoMovimiento;
import com.gestioninventario.backend.domain.entity.MovimientoStock.Estado;

public class MovimientoStockResponseDTO {

    private Long id_movimiento;

    private TipoMovimiento tipo_movimiento;

    private Long id_producto;
    private String nombre_producto;

    private Integer cantidad;

    private String documento_referencia;
    private String motivo;

    private Long id_usuario;
    private String nombre_usuario;

    private LocalDateTime fecha_movimiento;
    private Estado estado;

    public MovimientoStockResponseDTO() {
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

    public Long getId_producto() {
        return id_producto;
    }

    public void setId_producto(Long id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
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

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public LocalDateTime getFecha_movimiento() {
        return fecha_movimiento;
    }

    public void setFecha_movimiento(LocalDateTime fecha_movimiento) {
        this.fecha_movimiento = fecha_movimiento;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}