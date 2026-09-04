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
import com.gestioninventario.backend.domain.exception.EstadoSinCambiosException;
import com.gestioninventario.backend.domain.exception.RecursoDuplicadoException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final RolRepository rolRepository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, RolRepository rolRepository, UsuarioMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.rolRepository = rolRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    private void validarRolActivo(Rol rol) {
        if (rol.getEstado() == Rol.Estado.INACTIVO) {
            throw new IllegalArgumentException("No se puede crear un usuario con un rol inactivo");
        }
    }

    private void validarDatosExistentes(UsuarioCreateDTO usuarioDto) {
        if (repository.existsByNombreUsuario(usuarioDto.getNombre_usuario())) {
            throw new RecursoDuplicadoException("El nombre de usuario ya esta en uso");
        }

        if (repository.existsByCorreoElectronico(usuarioDto.getCorreo_electronico())) {
            throw new RecursoDuplicadoException("El correo electronico ya esta en uso");
        }

        if (repository.existsByTelefono(usuarioDto.getTelefono())) {
            throw new RecursoDuplicadoException("El teléfono ya está registrado");
        }
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
        validarDatosExistentes(usuarioDto);

        Rol rol = rolRepository.findById(usuarioDto.getId_rol())
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + usuarioDto.getId_rol() + " no existe"));
        
        validarRolActivo(rol);

        Usuario usuario = mapper.toEntity(usuarioDto, rol);

        usuario.setContrasena(passwordEncoder.encode(usuarioDto.getContrasena()));

        Usuario usuarioGuardado = repository.save(usuario);

        return mapper.toResponseDTO(usuarioGuardado);
    }

    public UsuarioResponseDTO actualizarUsuario(Long id_usuario, UsuarioUpdateDTO usuarioDto) {
        Usuario usuario = repository.findById(id_usuario)
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + id_usuario + " no existe"));

        Rol rol = rolRepository.findById(usuarioDto.getId_rol())
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + usuarioDto.getId_rol() + " no existe"));
        
        validarRolActivo(rol);

        mapper.updateEntity(usuarioDto, usuario, rol);

        Usuario usuarioActualizado = repository.save(usuario);

        return mapper.toResponseDTO(usuarioActualizado);
    }

    public UsuarioResponseDTO cambiarEstado(Long id_usuario, Usuario.Estado estado) {

        Usuario usuario = repository.findById(id_usuario)
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + id_usuario + " no existe"));

        if (usuario.getEstado() == estado){
            String mensaje = switch(estado){
                case ACTIVO -> "El usuario ya esta activo";
                case INACTIVO -> "El usuario ya esta inactivo";
                case BLOQUEADO -> "El usuario ya esta bloqueado";
            };
            throw new EstadoSinCambiosException(mensaje);
        }

        if (estado == Usuario.Estado.ACTIVO) {
            validarRolActivo(usuario.getRol());
        }

        usuario.setEstado(estado);

        Usuario usuarioActualizado = repository.save(usuario);

        return mapper.toResponseDTO(usuarioActualizado);
    }
}