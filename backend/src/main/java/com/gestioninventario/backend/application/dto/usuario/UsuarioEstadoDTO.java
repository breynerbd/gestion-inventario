package com.gestioninventario.backend.application.dto.usuario;

import com.gestioninventario.backend.domain.entity.Usuario.Estado;

import jakarta.validation.constraints.NotNull;

public class UsuarioEstadoDTO {

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    public UsuarioEstadoDTO() {
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}