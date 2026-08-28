package com.gestioninventario.backend.service;

import com.gestioninventario.backend.dto.rol.RolCreateDTO;
import com.gestioninventario.backend.dto.rol.RolResponseDTO;
import com.gestioninventario.backend.dto.rol.RolUpdateDTO;
import com.gestioninventario.backend.entity.Rol;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    private final RolRepository repository;

    public RolService(RolRepository repository) {
        this.repository = repository;
    }

    public List<RolResponseDTO> listarRoles() {
        return repository.findAll().stream().map(this::rolResponse).toList();
    }

    public RolResponseDTO obtenerRol(Long id_rol) {

        Rol rol = repository.findById(id_rol)
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + id_rol + " no existe"));

        return rolResponse(rol);
    }

    public RolResponseDTO crearRol(RolCreateDTO rolDto) {
        Rol rol = new Rol();

        rol.setNombre_rol(rolDto.getNombre_rol());
        rol.setDescripcion(rolDto.getDescripcion());
        rol.setEstado(Rol.Estado.ACTIVO);

        Rol rolGuardado = repository.save(rol);

        return rolResponse(rolGuardado);
    }

    public RolResponseDTO actualizarRol(Long id_rol, RolUpdateDTO rolDto) {

        Rol rol = repository.findById(id_rol)
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + id_rol + " no existe"));

        rol.setNombre_rol(rolDto.getNombre_rol());
        rol.setDescripcion(rolDto.getDescripcion());
        rol.setEstado(rolDto.getEstado());

        Rol rolActualizado = repository.save(rol);

        return rolResponse(rolActualizado);
    }

    public void eliminarRol(Long id_rol) {

        Rol rol = repository.findById(id_rol)
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + id_rol + " no existe"));

        repository.delete(rol);
    }

     private RolResponseDTO rolResponse(Rol rol) {

        RolResponseDTO dto = new RolResponseDTO();

        dto.setId_rol(rol.getId_rol());
        dto.setNombre_rol(rol.getNombre_rol());
        dto.setDescripcion(rol.getDescripcion());
        dto.setEstado(rol.getEstado());

        return dto;
    }
}