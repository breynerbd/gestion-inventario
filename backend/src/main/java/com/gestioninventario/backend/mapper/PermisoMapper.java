package com.gestioninventario.backend.mapper;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.dto.permiso.PermisoCreateDTO;
import com.gestioninventario.backend.dto.permiso.PermisoResponseDTO;
import com.gestioninventario.backend.dto.permiso.PermisoUpdateDTO;
import com.gestioninventario.backend.entity.Permiso;

@Component
public class PermisoMapper {

    public Permiso toEntity(PermisoCreateDTO dto) {

        Permiso permiso = new Permiso();

        permiso.setCodigo_permiso(dto.getCodigo_permiso());
        permiso.setNombre_permiso(dto.getNombre_permiso());
        permiso.setModulo(dto.getModulo());
        permiso.setDescripcion(dto.getDescripcion());

        return permiso;
    }

    public void updateEntity(PermisoUpdateDTO dto, Permiso permiso) {

        permiso.setNombre_permiso(dto.getNombre_permiso());
        permiso.setModulo(dto.getModulo());
        permiso.setDescripcion(dto.getDescripcion());
    }

    public PermisoResponseDTO toResponseDTO(Permiso permiso) {

        PermisoResponseDTO dto = new PermisoResponseDTO();

        dto.setId_permiso(permiso.getId_permiso());
        dto.setCodigo_permiso(permiso.getCodigo_permiso());
        dto.setNombre_permiso(permiso.getNombre_permiso());
        dto.setModulo(permiso.getModulo());
        dto.setDescripcion(permiso.getDescripcion());

        return dto;
    }
}