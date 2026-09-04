package com.gestioninventario.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.application.dto.rolPermiso.RolPermisoResponseDTO;
import com.gestioninventario.backend.domain.entity.Permiso;
import com.gestioninventario.backend.domain.entity.RolPermiso;

@Component
public class RolPermisoMapper {

    public RolPermisoResponseDTO toResponseDTO(RolPermiso rolPermiso) {

        RolPermisoResponseDTO dto = new RolPermisoResponseDTO();

        Permiso permiso = rolPermiso.getPermiso();

        dto.setId_permiso(permiso.getId_permiso());
        dto.setCodigo_permiso(permiso.getCodigo_permiso());
        dto.setNombre_permiso(permiso.getNombre_permiso());
        dto.setModulo(permiso.getModulo());
        dto.setDescripcion(permiso.getDescripcion());
        dto.setEstado(permiso.getEstado());
        dto.setFecha_asignacion(rolPermiso.getFecha_asignacion());

        return dto;
    }
}