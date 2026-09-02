package com.gestioninventario.backend.application.dto.auth;

public class LoginResponseDTO {

    private String access_token;
    private String refresh_token;
    private String tipo_token;
    private Long id_usuario;
    private String nombre_usuario;
    private String nombre_rol;

    public LoginResponseDTO() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getTipo_token() {
        return tipo_token;
    }

    public void setTipo_token(String tipo_token) {
        this.tipo_token = tipo_token;
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

    public String getNombre_rol() {
        return nombre_rol;
    }

    public void setNombre_rol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }
}