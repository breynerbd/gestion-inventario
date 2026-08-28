package com.gestioninventario.backend.dto.movimiento;

import com.gestioninventario.backend.entity.MovimientoStock.TipoMovimiento;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MovimientoStockUpdateDTO {

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimiento tipo_movimiento;

    @NotNull(message = "El producto es obligatorio")
    private Long id_producto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor que 0")
    private Integer cantidad;

    @Size(max = 30, message = "El documento de referencia no puede superar los 30 caracteres")
    private String documento_referencia;

    @Size(max = 250, message = "El motivo no puede superar los 250 caracteres")
    private String motivo;

    @NotNull(message = "El usuario es obligatorio")
    private Long id_usuario;

    public MovimientoStockUpdateDTO() {
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
}