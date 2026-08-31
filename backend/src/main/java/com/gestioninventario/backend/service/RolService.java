package com.gestioninventario.backend.service;

import com.gestioninventario.backend.dto.rol.RolCreateDTO;
import com.gestioninventario.backend.dto.rol.RolResponseDTO;
import com.gestioninventario.backend.dto.rol.RolUpdateDTO;
import com.gestioninventario.backend.entity.Rol;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.mapper.RolMapper;
import com.gestioninventario.backend.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    private final RolRepository repository;
    private final RolMapper mapper;

    public RolService(RolRepository repository, RolMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<RolResponseDTO> listarRoles() {
        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public RolResponseDTO obtenerRol(Long id_rol) {

        Rol rol = repository.findById(id_rol)
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + id_rol + " no existe"));

        return mapper.toResponseDTO(rol);
    }

    public RolResponseDTO crearRol(RolCreateDTO rolDto) {
        Rol rol = mapper.toEntity(rolDto);

        Rol rolGuardado = repository.save(rol);

        return mapper.toResponseDTO(rolGuardado);
    }

    public RolResponseDTO actualizarRol(Long id_rol, RolUpdateDTO rolDto) {

        Rol rol = repository.findById(id_rol)
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + id_rol + " no existe"));

        mapper.updateEntity(rolDto, rol);

        Rol rolActualizado = repository.save(rol);

        return mapper.toResponseDTO(rolActualizado);
    }

    public void eliminarRol(Long id_rol) {

        Rol rol = repository.findById(id_rol)
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + id_rol + " no existe"));

        repository.delete(rol);
    }
}