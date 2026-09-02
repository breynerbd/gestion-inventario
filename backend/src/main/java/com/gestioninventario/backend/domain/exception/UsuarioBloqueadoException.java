package com.gestioninventario.backend.domain.exception;

public class UsuarioBloqueadoException extends RuntimeException {

    public UsuarioBloqueadoException(String message) {
        super(message);
    }
}