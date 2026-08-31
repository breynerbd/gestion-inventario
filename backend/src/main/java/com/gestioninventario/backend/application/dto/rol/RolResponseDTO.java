package com.gestioninventario.backend.application.dto.rol;

import com.gestioninventario.backend.domain.entity.Rol.Estado;

public class RolResponseDTO {

    private Long id_rol;

    private String nombre_rol;

    private String descripcion;

    private Estado estado;

    public RolResponseDTO() {
    }

    public Long getId_rol() {
        return id_rol;
    }

    public void setId_rol(Long id_rol) {
        this.id_rol = id_rol;
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}