package com.gestioninventario.backend.presentation.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gestioninventario.backend.domain.exception.CredencialesInvalidasException;
import com.gestioninventario.backend.domain.exception.RecursoDuplicadoException;
import com.gestioninventario.backend.domain.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.domain.exception.UsuarioBloqueadoException;
import com.gestioninventario.backend.domain.exception.UsuarioInactivoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(RecursoNoEncontradoException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresValidacion(MethodArgumentNotValidException exception) {

        Map<String, String> errores = new HashMap<>();

        exception.getBindingResult().getFieldErrors()
            .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoDuplicado(RecursoDuplicadoException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<Map<String, String>>
    manejarCredencialesInvalidas(CredencialesInvalidasException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }

    @ExceptionHandler(UsuarioBloqueadoException.class)
    public ResponseEntity<Map<String, String>>
    manejarUsuarioBloqueado(UsuarioBloqueadoException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }

    @ExceptionHandler(UsuarioInactivoException.class)
    public ResponseEntity<Map<String, String>>
    manejarUsuarioInactivo(UsuarioInactivoException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }
}