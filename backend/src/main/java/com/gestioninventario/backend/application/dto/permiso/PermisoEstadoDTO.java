package com.gestioninventario.backend.application.dto.permiso;

import com.gestioninventario.backend.domain.entity.Permiso.Estado;

import jakarta.validation.constraints.NotNull;

public class PermisoEstadoDTO {

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    public PermisoEstadoDTO() {
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}