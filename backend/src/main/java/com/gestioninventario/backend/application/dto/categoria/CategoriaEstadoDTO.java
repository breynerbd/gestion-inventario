package com.gestioninventario.backend.application.dto.categoria;

import com.gestioninventario.backend.domain.entity.Categoria.Estado;

import jakarta.validation.constraints.NotNull;

public class CategoriaEstadoDTO {

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    public CategoriaEstadoDTO() {
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}