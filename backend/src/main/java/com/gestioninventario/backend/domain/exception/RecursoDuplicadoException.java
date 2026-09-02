package com.gestioninventario.backend.domain.exception;

public class RecursoDuplicadoException extends RuntimeException {

    public RecursoDuplicadoException(String message) {
        super(message);
    }
}