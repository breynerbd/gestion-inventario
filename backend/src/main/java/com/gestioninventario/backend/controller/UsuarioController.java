package com.gestioninventario.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gestioninventario.backend.entity.Usuario;
import com.gestioninventario.backend.service.UsuarioService;

@Controller
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        return ResponseEntity.ok(service.listarUsuarios());
    }

    @GetMapping("/{id_usuario}")
    public ResponseEntity<Usuario> listarUsuarioPorId(@PathVariable("id_usuario") Long id_usuario){
        return ResponseEntity.ok(service.obtenerUsuario(id_usuario));
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearUsuario(usuario));
    }

    @PutMapping("/{id_usuario}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable("id_usuario") Long id_usuario, @RequestBody Usuario usuarioActualizado){
        return ResponseEntity.ok(service.actualizarUsuario(id_usuario, usuarioActualizado));
    }

    @DeleteMapping("/{id_usuario}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable("id_usuario") Long id_usuario){
        service.eliminarUsuario(id_usuario);
        return ResponseEntity.noContent().build();
    }
}