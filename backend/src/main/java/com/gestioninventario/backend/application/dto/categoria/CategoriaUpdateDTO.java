package com.gestioninventario.backend.application.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CategoriaUpdateDTO {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 60, message = "El nombre de la categoría no puede superar los 60 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚÑáéíóúñ\\s]+$", message = "El nombre de la categoría solo puede tener letras y espacios")
    private String nombre_categoria;

    @Size(max = 250, message = "La descripción no puede superar los 250 caracteres")
    private String descripcion;

    public CategoriaUpdateDTO() {
    }

    public String getNombre_categoria() {
        return nombre_categoria;
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}