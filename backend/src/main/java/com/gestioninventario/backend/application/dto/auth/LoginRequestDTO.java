package com.gestioninventario.backend.application.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombre_usuario;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    public LoginRequestDTO() {
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
}