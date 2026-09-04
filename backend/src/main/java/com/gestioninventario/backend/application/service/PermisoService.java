package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.application.dto.permiso.PermisoCreateDTO;
import com.gestioninventario.backend.application.dto.permiso.PermisoResponseDTO;
import com.gestioninventario.backend.application.dto.permiso.PermisoUpdateDTO;
import com.gestioninventario.backend.domain.entity.Permiso;
import com.gestioninventario.backend.domain.exception.EstadoSinCambiosException;
import com.gestioninventario.backend.domain.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.application.mapper.PermisoMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.PermisoRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermisoService {

    private final PermisoRepository repository;
    private final PermisoMapper mapper;

    public PermisoService(PermisoRepository repository, PermisoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PermisoResponseDTO> listarPermisos() {
        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public PermisoResponseDTO obtenerPermiso(Long id_permiso) {
        Permiso permiso = repository.findById(id_permiso)
            .orElseThrow(() -> new RecursoNoEncontradoException("El permiso " + id_permiso + " no existe"));

        return mapper.toResponseDTO(permiso);
    }

    public PermisoResponseDTO crearPermiso(PermisoCreateDTO permisoDto) {

        Permiso permiso = mapper.toEntity(permisoDto);

        Permiso permisoGuardado = repository.save(permiso);

        return mapper.toResponseDTO(permisoGuardado);
    }

    public PermisoResponseDTO actualizarPermiso(Long id_permiso, PermisoUpdateDTO permisoDto) {

        Permiso permiso = repository.findById(id_permiso)
            .orElseThrow(() -> new RecursoNoEncontradoException("El permiso " + id_permiso + " no existe"));

        mapper.updateEntity(permisoDto, permiso);

        Permiso permisoActualizado = repository.save(permiso);

        return mapper.toResponseDTO(permisoActualizado);
    }

    public PermisoResponseDTO cambiarEstado(Long id_permiso, Permiso.Estado estado) {
        Permiso permiso = repository.findById(id_permiso)
            .orElseThrow(() -> new RecursoNoEncontradoException("El permiso " + id_permiso + " no existe"));

        if (permiso.getEstado() == estado) {
            String mensaje = switch (estado) {
                case ACTIVO -> "El permiso ya esta activo";
                case INACTIVO -> "El permiso ya esta inactivo";
            };

            throw new EstadoSinCambiosException(mensaje);
        }

        permiso.setEstado(estado);

        Permiso permisoActualizado = repository.save(permiso);

        return mapper.toResponseDTO(permisoActualizado);
    }
}