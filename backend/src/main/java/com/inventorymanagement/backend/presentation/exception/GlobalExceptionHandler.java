package com.inventorymanagement.backend.presentation.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.inventorymanagement.backend.domain.exception.InvalidCredentialsException;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.domain.exception.DuplicateResourceException;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.domain.exception.BlockedUserException;
import com.inventorymanagement.backend.domain.exception.InactiveUserException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_KEY = "error";
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException exception) {
        LOGGER.warn("No se encontro el recurso solicitado");

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException exception) {
        LOGGER.warn("Hay datos que no son validos");

        Map<String, String> errores = new HashMap<>();

        exception.getBindingResult().getFieldErrors()
            .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateResource(DuplicateResourceException exception) {
        LOGGER.warn("Se intento duplicar un dato");

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException exception) {
        LOGGER.warn("Las credenciales son incorrectas");

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }

    @ExceptionHandler(BlockedUserException.class)
    public ResponseEntity<Map<String, String>> handleBlockedUser(BlockedUserException exception) {
        LOGGER.warn("Se intento acceder con un usuario bloqueado");

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }

    @ExceptionHandler(InactiveUserException.class)
    public ResponseEntity<Map<String, String>> handleInactiveUser(InactiveUserException exception) {
        LOGGER.warn("Se intento acceder con un usuario inactivo");

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }

    @ExceptionHandler(StatusUnchangedException.class)
    public ResponseEntity<Map<String, String>> handleStatusUnChange(StatusUnchangedException exception) {
        LOGGER.warn("Se intento asignar un estado que ya estaba");

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIlegalArgument(IllegalArgumentException exception) {
        LOGGER.warn("Se recibio un dato que no es valido");

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
}