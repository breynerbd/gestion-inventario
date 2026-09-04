package com.gestioninventario.backend.domain.exception;

public class EstadoSinCambiosException extends RuntimeException {

    public EstadoSinCambiosException(String message) {
        super(message);
    }
}