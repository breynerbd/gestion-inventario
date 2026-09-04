package com.gestioninventario.backend.application.dto.rolPermiso;

import java.time.LocalDateTime;

import com.gestioninventario.backend.domain.entity.Permiso;

public class RolPermisoResponseDTO {

    private Long id_permiso;
    private String codigo_permiso;
    private String nombre_permiso;
    private Permiso.Modulo modulo;
    private String descripcion;
    private Permiso.Estado estado;
    private LocalDateTime fecha_asignacion;

    public Long getId_permiso() {
        return id_permiso;
    }

    public void setId_permiso(Long id_permiso) {
        this.id_permiso = id_permiso;
    }

    public String getCodigo_permiso() {
        return codigo_permiso;
    }

    public void setCodigo_permiso(String codigo_permiso) {
        this.codigo_permiso = codigo_permiso;
    }

    public String getNombre_permiso() {
        return nombre_permiso;
    }

    public void setNombre_permiso(String nombre_permiso) {
        this.nombre_permiso = nombre_permiso;
    }

    public Permiso.Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Permiso.Modulo modulo) {
        this.modulo = modulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Permiso.Estado getEstado() {
        return estado;
    }

    public void setEstado(Permiso.Estado estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha_asignacion() {
        return fecha_asignacion;
    }

    public void setFecha_asignacion(LocalDateTime fecha_asignacion) {
        this.fecha_asignacion = fecha_asignacion;
    }
}