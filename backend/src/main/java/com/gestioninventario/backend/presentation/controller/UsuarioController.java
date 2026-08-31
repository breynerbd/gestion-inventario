package com.gestioninventario.backend.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestioninventario.backend.application.dto.usuario.UsuarioCreateDTO;
import com.gestioninventario.backend.application.dto.usuario.UsuarioResponseDTO;
import com.gestioninventario.backend.application.dto.usuario.UsuarioUpdateDTO;
import com.gestioninventario.backend.application.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){
        return ResponseEntity.ok(service.listarUsuarios());
    }

    @GetMapping("/{id_usuario}")
    public ResponseEntity<UsuarioResponseDTO> listarUsuarioPorId(@PathVariable("id_usuario") Long id_usuario){
        return ResponseEntity.ok(service.obtenerUsuario(id_usuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioCreateDTO usuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearUsuario(usuario));
    }

    @PutMapping("/{id_usuario}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable("id_usuario") Long id_usuario, @Valid @RequestBody UsuarioUpdateDTO usuarioActualizado){
        return ResponseEntity.ok(service.actualizarUsuario(id_usuario, usuarioActualizado));
    }

    @DeleteMapping("/{id_usuario}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable("id_usuario") Long id_usuario){
        service.eliminarUsuario(id_usuario);
        return ResponseEntity.noContent().build();
    }
}