package com.gestioninventario.backend.application.dto.rol;

import com.gestioninventario.backend.domain.entity.Rol.Estado;

import jakarta.validation.constraints.NotNull;

public class RolEstadoDTO {

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    public RolEstadoDTO() {
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}