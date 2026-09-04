package com.gestioninventario.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.application.dto.rol.RolCreateDTO;
import com.gestioninventario.backend.application.dto.rol.RolResponseDTO;
import com.gestioninventario.backend.application.dto.rol.RolUpdateDTO;
import com.gestioninventario.backend.domain.entity.Rol;

@Component
public class RolMapper {

    public Rol toEntity(RolCreateDTO dto) {

        Rol rol = new Rol();

        rol.setNombre_rol(dto.getNombre_rol());
        rol.setDescripcion(dto.getDescripcion());
        rol.setEstado(Rol.Estado.ACTIVO);

        return rol;
    }

    public void updateEntity(RolUpdateDTO dto, Rol rol) {

        rol.setNombre_rol(dto.getNombre_rol());
        rol.setDescripcion(dto.getDescripcion());
    }

    public RolResponseDTO toResponseDTO(Rol rol) {

        RolResponseDTO dto = new RolResponseDTO();

        dto.setId_rol(rol.getId_rol());
        dto.setNombre_rol(rol.getNombre_rol());
        dto.setDescripcion(rol.getDescripcion());
        dto.setEstado(rol.getEstado());

        return dto;
    }
}