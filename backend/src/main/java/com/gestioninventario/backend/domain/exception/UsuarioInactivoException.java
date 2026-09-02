package com.gestioninventario.backend.domain.exception;

public class UsuarioInactivoException extends RuntimeException {

    public UsuarioInactivoException(String message) {
        super(message);
    }
}