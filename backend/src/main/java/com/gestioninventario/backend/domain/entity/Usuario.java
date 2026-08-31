package com.gestioninventario.backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    public enum Estado {
        ACTIVO, 
        INACTIVO,
        BLOQUEADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id_usuario;

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 30)
    private String nombre_usuario;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "nombres", nullable = false, length = 60)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 60)
    private String apellidos;

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 100)
    private String correo_electronico;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private Estado estado = Estado.ACTIVO;

    @Column(name = "intentos_fallidos")
    private Integer intentos_fallidos;

    public Usuario() {
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Integer getIntentos_fallidos() {
        return intentos_fallidos;
    }

    public void setIntentos_fallidos(Integer intentos_fallidos) {
        this.intentos_fallidos = intentos_fallidos;
    }
}