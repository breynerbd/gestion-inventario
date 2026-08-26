package com.gestioninventario.backend.service;

import com.gestioninventario.backend.entity.Usuario;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    public Usuario obtenerUsuario(Long id_usuario) {

        return repository.findById(id_usuario)
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + id_usuario + " no existe"));
    }

    public Usuario crearUsuario(Usuario usuario) {
        return repository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id_usuario, Usuario usuarioActualizado) {

        Usuario usuario = repository.findById(id_usuario)
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + id_usuario + " no existe"));

        usuario.setNombre_usuario(usuarioActualizado.getNombre_usuario());
        usuario.setContrasena(usuarioActualizado.getContrasena());
        usuario.setNombres(usuarioActualizado.getNombres());
        usuario.setApellidos(usuarioActualizado.getApellidos());
        usuario.setCorreo_electronico(usuarioActualizado.getCorreo_electronico());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setRol(usuarioActualizado.getRol());
        usuario.setEstado(usuarioActualizado.getEstado());
        usuario.setIntentos_fallidos(usuarioActualizado.getIntentos_fallidos());

        return repository.save(usuario);
    }

    public void eliminarUsuario(Long id_usuario) {

        Usuario usuario = repository.findById(id_usuario)
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + id_usuario + " no existe"));

        repository.delete(usuario);
    }
}