package com.gestioninventario.backend.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaCreateDTO {

    @NotBlank(message = "El código de la categoría es obligatorio")
    @Size(max = 10, message = "El código de la categoría no puede superar los 10 caracteres")
    private String codigo_categoria;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 60, message = "El nombre de la categoría no puede superar los 60 caracteres")
    private String nombre_categoria;

    @Size(max = 250, message = "La descripción no puede superar los 250 caracteres")
    private String descripcion;

    public CategoriaCreateDTO() {
    }

    public String getCodigo_categoria() {
        return codigo_categoria;
    }

    public void setCodigo_categoria(String codigo_categoria) {
        this.codigo_categoria = codigo_categoria;
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