package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.application.dto.usuario.UsuarioCreateDTO;
import com.gestioninventario.backend.application.dto.usuario.UsuarioResponseDTO;
import com.gestioninventario.backend.application.dto.usuario.UsuarioUpdateDTO;
import com.gestioninventario.backend.domain.entity.Rol;
import com.gestioninventario.backend.domain.entity.Usuario;
import com.gestioninventario.backend.domain.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.application.mapper.UsuarioMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.RolRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final RolRepository rolRepository;
    private final UsuarioMapper mapper;

    public UsuarioService(UsuarioRepository repository, RolRepository rolRepository, UsuarioMapper mapper) {
        this.repository = repository;
        this.rolRepository = rolRepository;
        this.mapper = mapper;
    }

    public List<UsuarioResponseDTO> listarUsuarios() {

        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public UsuarioResponseDTO obtenerUsuario(Long id_usuario) {

        Usuario usuario = repository.findById(id_usuario)
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + id_usuario + " no existe"));

        return mapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO crearUsuario(UsuarioCreateDTO usuarioDto) {
        Rol rol = rolRepository.findById(usuarioDto.getId_rol())
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + usuarioDto.getId_rol() + " no existe"));

        Usuario usuario = mapper.toEntity(usuarioDto, rol);

        Usuario usuarioGuardado = repository.save(usuario);

        return mapper.toResponseDTO(usuarioGuardado);
    }

    public UsuarioResponseDTO actualizarUsuario(Long id_usuario, UsuarioUpdateDTO usuarioDto) {
        Usuario usuario = repository.findById(id_usuario)
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + id_usuario + " no existe"));

        Rol rol = rolRepository.findById(usuarioDto.getId_rol())
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + usuarioDto.getId_rol() + " no existe"));

        mapper.updateEntity(usuarioDto, usuario, rol);

        Usuario usuarioActualizado = repository.save(usuario);

        return mapper.toResponseDTO(usuarioActualizado);
    }

    public void eliminarUsuario(Long id_usuario) {

        Usuario usuario = repository.findById(id_usuario)
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + id_usuario + " no existe"));

        repository.delete(usuario);
    }
}