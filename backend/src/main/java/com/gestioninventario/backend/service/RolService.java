package com.gestioninventario.backend.service;

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

    public List<Rol> listarRoles() {
        return repository.findAll();
    }

    public Rol obtenerRol(Long id_rol) {

        return repository.findById(id_rol)
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + id_rol + " no existe"));
    }

    public Rol crearRol(Rol rol) {
        return repository.save(rol);
    }

    public Rol actualizarRol(Long id_rol, Rol rolActualizado) {

        Rol rol = repository.findById(id_rol)
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + id_rol + " no existe"));

        rol.setNombre_rol(rolActualizado.getNombre_rol());
        rol.setDescripcion(rolActualizado.getDescripcion());
        rol.setEstado(rolActualizado.getEstado());

        return repository.save(rol);
    }

    public void eliminarRol(Long id_rol) {

        Rol rol = repository.findById(id_rol)
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + id_rol + " no existe"));

        repository.delete(rol);
    }
}