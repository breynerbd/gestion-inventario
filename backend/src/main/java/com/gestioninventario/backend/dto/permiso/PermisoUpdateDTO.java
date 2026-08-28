package com.gestioninventario.backend.dto.permiso;

import com.gestioninventario.backend.entity.Permiso.Modulo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PermisoUpdateDTO {

    @NotBlank(message = "El nombre del permiso es obligatorio")
    @Size(max = 100, message = "El nombre del permiso no puede superar los 100 caracteres")
    private String nombre_permiso;

    @NotNull(message = "El módulo es obligatorio")
    private Modulo modulo;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String descripcion;

    public PermisoUpdateDTO() {
    }

    public String getNombre_permiso() {
        return nombre_permiso;
    }

    public void setNombre_permiso(String nombre_permiso) {
        this.nombre_permiso = nombre_permiso;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}