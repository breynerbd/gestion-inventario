package com.gestioninventario.backend.application.dto.movimiento;

import com.gestioninventario.backend.domain.entity.MovimientoStock.Estado;

import jakarta.validation.constraints.NotNull;

public class MovimientoStockEstadoDTO {

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    public MovimientoStockEstadoDTO() {
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}