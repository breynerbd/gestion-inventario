package com.gestioninventario.backend.application.dto.rol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RolCreateDTO {

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50, message = "El nombre del rol no puede superar los 50 caracteres")
    private String nombre_rol;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String descripcion;

    public RolCreateDTO() {
    }

    public String getNombre_rol() {
        return nombre_rol;
    }

    public void setNombre_rol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}