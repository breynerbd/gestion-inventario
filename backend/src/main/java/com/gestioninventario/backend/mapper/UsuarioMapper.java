package com.gestioninventario.backend.mapper;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.dto.usuario.UsuarioCreateDTO;
import com.gestioninventario.backend.dto.usuario.UsuarioResponseDTO;
import com.gestioninventario.backend.dto.usuario.UsuarioUpdateDTO;
import com.gestioninventario.backend.entity.Rol;
import com.gestioninventario.backend.entity.Usuario;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioCreateDTO dto, Rol rol) {

        Usuario usuario = new Usuario();

        usuario.setNombre_usuario(dto.getNombre_usuario());
        usuario.setContrasena(dto.getContrasena());
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setCorreo_electronico(dto.getCorreo_electronico());
        usuario.setTelefono(dto.getTelefono());
        usuario.setEstado(Usuario.Estado.ACTIVO);
        usuario.setIntentos_fallidos(0);
        usuario.setRol(rol);

        return usuario;
    }

    public void updateEntity(UsuarioUpdateDTO dto, Usuario usuario, Rol rol) {

        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setCorreo_electronico(dto.getCorreo_electronico());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(rol);
        usuario.setEstado(dto.getEstado());
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {

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