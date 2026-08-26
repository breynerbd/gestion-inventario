package com.gestioninventario.backend.service;

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

    public List<Permiso> listarPermisos() {
        return repository.findAll();
    }

    public Permiso obtenerPermiso(Long id_permiso) {

        return repository.findById(id_permiso)
            .orElseThrow(() -> new RecursoNoEncontradoException("El permiso " + id_permiso + " no existe"));
    }

    public Permiso crearPermiso(Permiso permiso) {
        return repository.save(permiso);
    }

    public Permiso actualizarPermiso(Long id_permiso, Permiso permisoActualizado) {

        Permiso permiso = repository.findById(id_permiso)
            .orElseThrow(() -> new RecursoNoEncontradoException("El permiso " + id_permiso + " no existe"));

        permiso.setCodigo_permiso(permisoActualizado.getCodigo_permiso());
        permiso.setNombre_permiso(permisoActualizado.getNombre_permiso());
        permiso.setModulo(permisoActualizado.getModulo());
        permiso.setDescripcion(permisoActualizado.getDescripcion());

        return repository.save(permiso);
    }

    public void eliminarPermiso(Long id_permiso) {

        Permiso permiso = repository.findById(id_permiso)
            .orElseThrow(() -> new RecursoNoEncontradoException("El permiso " + id_permiso + " no existe"));

        repository.delete(permiso);
    }
}