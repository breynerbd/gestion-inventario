package com.gestioninventario.backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "permisos")
public class Permiso {

    public enum Modulo {
        PRODUCTOS,
        CATEGORIAS,
        PROVEEDORES,
        MOVIMIENTOS,
        USUARIOS,
        ROLES,
        REPORTES
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Long id_permiso;

    @Column(name = "codigo_permiso", nullable = false, unique = true, length = 60)
    private String codigo_permiso;

    @Column(name = "nombre_permiso", nullable = false, length = 100)
    private String nombre_permiso;

    @Enumerated(EnumType.STRING)
    @Column(name = "modulo", nullable = false, length = 30)
    private Modulo modulo;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    public Permiso() {
    }

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