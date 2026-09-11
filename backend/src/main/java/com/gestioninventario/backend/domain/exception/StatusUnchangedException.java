package com.gestioninventario.backend.domain.exception;

public class StatusUnchangedException extends RuntimeException {

    public StatusUnchangedException(String message) {
        super(message);
    }
}