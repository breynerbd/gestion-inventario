package com.gestioninventario.backend.application.dto.rolPermiso;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public class RolPermisoDTO {

    @NotNull(message = "La lista de permisos es obligatoria")
    private List<Long> permisos;

    public List<Long> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Long> permisos) {
        this.permisos = permisos;
    }
}