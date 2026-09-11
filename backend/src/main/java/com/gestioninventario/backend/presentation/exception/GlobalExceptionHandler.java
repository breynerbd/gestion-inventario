package com.gestioninventario.backend.presentation.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gestioninventario.backend.domain.exception.InvalidCredentialsException;
import com.gestioninventario.backend.domain.exception.StatusUnchangedException;
import com.gestioninventario.backend.domain.exception.DuplicateResourceException;
import com.gestioninventario.backend.domain.exception.ResourceNotFoundException;
import com.gestioninventario.backend.domain.exception.BlockedUserException;
import com.gestioninventario.backend.domain.exception.InactiveUserException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_KEY = "error";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(ResourceNotFoundException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresValidacion(MethodArgumentNotValidException exception) {

        Map<String, String> errores = new HashMap<>();

        exception.getBindingResult().getFieldErrors()
            .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoDuplicado(DuplicateResourceException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>>
    manejarCredencialesInvalidas(InvalidCredentialsException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }

    @ExceptionHandler(BlockedUserException.class)
    public ResponseEntity<Map<String, String>>
    manejarUsuarioBloqueado(BlockedUserException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }

    @ExceptionHandler(InactiveUserException.class)
    public ResponseEntity<Map<String, String>>
    manejarUsuarioINACTIVO(InactiveUserException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }

    @ExceptionHandler(StatusUnchangedException.class)
    public ResponseEntity<Map<String, String>> manejarEstadoSinCambios(StatusUnchangedException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarArgumentoInvalido(IllegalArgumentException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(ERROR_KEY, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
}