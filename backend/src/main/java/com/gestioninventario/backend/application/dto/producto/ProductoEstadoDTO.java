package com.gestioninventario.backend.application.dto.producto;

import com.gestioninventario.backend.domain.entity.Producto.Estado;

import jakarta.validation.constraints.NotNull;

public class ProductoEstadoDTO {

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    public ProductoEstadoDTO() {
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}