package com.gestioninventario.backend.application.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequestDTO {

    @NotBlank(message = "El refresh token es obligatorio")
    private String refresh_token;

    public RefreshTokenRequestDTO() {
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}