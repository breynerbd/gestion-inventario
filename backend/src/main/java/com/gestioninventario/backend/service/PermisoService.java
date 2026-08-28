package com.gestioninventario.backend.service;

import com.gestioninventario.backend.dto.permiso.PermisoCreateDTO;
import com.gestioninventario.backend.dto.permiso.PermisoResponseDTO;
import com.gestioninventario.backend.dto.permiso.PermisoUpdateDTO;
import com.gestioninventario.backend.entity.Permiso;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.repository.PermisoRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermisoService {

    private final PermisoRepository repository;

    public PermisoService(PermisoRepository repository) {
        this.repository = repository;
    }

    public List<PermisoResponseDTO> listarPermisos() {
        return repository.findAll().stream().map(this::permisoResponse).toList();
    }

    public PermisoResponseDTO obtenerPermiso(Long id_permiso) {
        Permiso permiso = repository.findById(id_permiso)
            .orElseThrow(() -> new RecursoNoEncontradoException("El permiso " + id_permiso + " no existe"));

        return permisoResponse(permiso);
    }

    public PermisoResponseDTO crearPermiso(PermisoCreateDTO permisoDto) {

        Permiso permiso = new Permiso();

        permiso.setCodigo_permiso(permisoDto.getCodigo_permiso());
        permiso.setNombre_permiso(permisoDto.getNombre_permiso());
        permiso.setModulo(permisoDto.getModulo());
        permiso.setDescripcion(permisoDto.getDescripcion());

        Permiso permisoGuardado = repository.save(permiso);

        return permisoResponse(permisoGuardado);
    }

    public PermisoResponseDTO actualizarPermiso(Long id_permiso, PermisoUpdateDTO permisoDto) {

        Permiso permiso = repository.findById(id_permiso)
            .orElseThrow(() -> new RecursoNoEncontradoException("El permiso " + id_permiso + " no existe"));

        permiso.setNombre_permiso(permisoDto.getNombre_permiso());
        permiso.setModulo(permisoDto.getModulo());
        permiso.setDescripcion(permisoDto.getDescripcion());

        Permiso permisoActualizado = repository.save(permiso);

        return permisoResponse(permisoActualizado);
    }

    public void eliminarPermiso(Long id_permiso) {
        Permiso permiso = repository.findById(id_permiso)
            .orElseThrow(() -> new RecursoNoEncontradoException("El permiso " + id_permiso + " no existe"));

        repository.delete(permiso);
    }

    private PermisoResponseDTO permisoResponse(Permiso permiso) {
        PermisoResponseDTO dto = new PermisoResponseDTO();

        dto.setId_permiso(permiso.getId_permiso());
        dto.setCodigo_permiso(permiso.getCodigo_permiso());
        dto.setNombre_permiso(permiso.getNombre_permiso());
        dto.setModulo(permiso.getModulo());
        dto.setDescripcion(permiso.getDescripcion());

        return dto;
    }
}