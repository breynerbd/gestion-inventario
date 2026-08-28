package com.gestioninventario.backend.dto.categoria;

import com.gestioninventario.backend.entity.Categoria;

import java.time.LocalDateTime;

public class CategoriaResponseDTO {

    private Long id_categoria;
    private String codigo_categoria;
    private String nombre_categoria;
    private String descripcion;
    private Categoria.Estado estado;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_modificacion;

    public CategoriaResponseDTO() {
    }

    public Long getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(Long id_categoria) {
        this.id_categoria = id_categoria;
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

    public Categoria.Estado getEstado() {
        return estado;
    }

    public void setEstado(Categoria.Estado estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public LocalDateTime getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(LocalDateTime fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }
}