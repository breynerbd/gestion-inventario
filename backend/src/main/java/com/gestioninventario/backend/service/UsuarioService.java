package com.gestioninventario.backend.service;

import com.gestioninventario.backend.dto.usuario.UsuarioCreateDTO;
import com.gestioninventario.backend.dto.usuario.UsuarioResponseDTO;
import com.gestioninventario.backend.dto.usuario.UsuarioUpdateDTO;
import com.gestioninventario.backend.entity.Rol;
import com.gestioninventario.backend.entity.Usuario;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.repository.RolRepository;
import com.gestioninventario.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository repository, RolRepository rolRepository) {
        this.repository = repository;
        this.rolRepository = rolRepository;
    }

    public List<UsuarioResponseDTO> listarUsuarios() {

        return repository.findAll().stream().map(this::usuarioResponse).toList();
    }

    public UsuarioResponseDTO obtenerUsuario(Long id_usuario) {

        Usuario usuario = repository.findById(id_usuario)
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + id_usuario + " no existe"));

        return usuarioResponse(usuario);
    }

    public UsuarioResponseDTO crearUsuario(UsuarioCreateDTO usuarioDto) {
        Rol rol = rolRepository.findById(usuarioDto.getId_rol())
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + usuarioDto.getId_rol() + " no existe"));

        Usuario usuario = new Usuario();

        usuario.setNombre_usuario(usuarioDto.getNombre_usuario());
        usuario.setContrasena(usuarioDto.getContrasena());
        usuario.setNombres(usuarioDto.getNombres());
        usuario.setApellidos(usuarioDto.getApellidos());
        usuario.setCorreo_electronico(usuarioDto.getCorreo_electronico());
        usuario.setTelefono(usuarioDto.getTelefono());
        usuario.setRol(rol);
        usuario.setEstado(Usuario.Estado.ACTIVO);
        usuario.setIntentos_fallidos(0);

        Usuario usuarioGuardado = repository.save(usuario);

        return usuarioResponse(usuarioGuardado);
    }

    public UsuarioResponseDTO actualizarUsuario(Long id_usuario, UsuarioUpdateDTO usuarioDto) {
        Usuario usuario = repository.findById(id_usuario)
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + id_usuario + " no existe"));

        Rol rol = rolRepository.findById(usuarioDto.getId_rol())
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + usuarioDto.getId_rol() + " no existe"));

        usuario.setNombres(usuarioDto.getNombres());
        usuario.setApellidos(usuarioDto.getApellidos());
        usuario.setCorreo_electronico(usuarioDto.getCorreo_electronico());
        usuario.setTelefono(usuarioDto.getTelefono());
        usuario.setRol(rol);
        usuario.setEstado(usuarioDto.getEstado());

        Usuario usuarioActualizado = repository.save(usuario);

        return usuarioResponse(usuarioActualizado);
    }

    public void eliminarUsuario(Long id_usuario) {

        Usuario usuario = repository.findById(id_usuario)
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + id_usuario + " no existe"));

        repository.delete(usuario);
    }

    private UsuarioResponseDTO usuarioResponse(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();

        dto.setId_usuario(usuario.getId_usuario());
        dto.setNombre_usuario(usuario.getNombre_usuario());
        dto.setNombres(usuario.getNombres());
        dto.setApellidos(usuario.getApellidos());
        dto.setCorreo_electronico(usuario.getCorreo_electronico());
        dto.setTelefono(usuario.getTelefono());

        if (usuario.getRol() != null) {
            dto.setId_rol(usuario.getRol().getId_rol());
            dto.setNombre_rol(usuario.getRol().getNombre_rol());
        }

        dto.setEstado(usuario.getEstado());
        dto.setIntentos_fallidos(usuario.getIntentos_fallidos());

        return dto;
    }
}