package com.gestioninventario.backend.dto.usuario;

import com.gestioninventario.backend.entity.Usuario.Estado;

public class UsuarioResponseDTO {

    private Long id_usuario;

    private String nombre_usuario;

    private String nombres;

    private String apellidos;

    private String correo_electronico;

    private String telefono;

    private Long id_rol;

    private String nombre_rol;

    private Estado estado;

    private Integer intentos_fallidos;

    public UsuarioResponseDTO() {
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