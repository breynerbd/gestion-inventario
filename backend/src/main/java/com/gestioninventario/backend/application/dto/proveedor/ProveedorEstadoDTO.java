package com.gestioninventario.backend.application.dto.proveedor;

import com.gestioninventario.backend.domain.entity.Proveedor.Estado;

import jakarta.validation.constraints.NotNull;

public class ProveedorEstadoDTO {

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    public ProveedorEstadoDTO() {
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}