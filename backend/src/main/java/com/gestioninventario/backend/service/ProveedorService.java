package com.gestioninventario.backend.service;

import com.gestioninventario.backend.entity.Proveedor;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository repository;

    public ProveedorService(ProveedorRepository repository) {
        this.repository = repository;
    }

    public List<Proveedor> listarProveedores() {
        return repository.findAll();
    }

    public Proveedor obtenerProveedor(Long id_proveedor) {

        return repository.findById(id_proveedor)
            .orElseThrow(() -> new RecursoNoEncontradoException("El proveedor " + id_proveedor + " no existe"));
    }

    public Proveedor crearProveedor(Proveedor proveedor) {
        return repository.save(proveedor);
    }

    public Proveedor actualizarProveedor(Long id_proveedor, Proveedor proveedorActualizado) {

        Proveedor proveedor = repository.findById(id_proveedor)
            .orElseThrow(() -> new RecursoNoEncontradoException("El proveedor " + id_proveedor + " no existe"));

        proveedor.setCodigo_proveedor(proveedorActualizado.getCodigo_proveedor());
        proveedor.setTipo_documento(proveedorActualizado.getTipo_documento());
        proveedor.setNumero_documento(proveedorActualizado.getNumero_documento());
        proveedor.setRazon_social(proveedorActualizado.getRazon_social());
        proveedor.setNombre_contacto(proveedorActualizado.getNombre_contacto());
        proveedor.setTelefono(proveedorActualizado.getTelefono());
        proveedor.setCorreo_electronico(proveedorActualizado.getCorreo_electronico());
        proveedor.setDireccion(proveedorActualizado.getDireccion());
        proveedor.setEstado(proveedorActualizado.getEstado());

        return repository.save(proveedor);
    }

    public void eliminarProveedor(Long id_proveedor) {

        Proveedor proveedor = repository.findById(id_proveedor)
            .orElseThrow(() -> new RecursoNoEncontradoException("El proveedor " + id_proveedor + " no existe"));

        repository.delete(proveedor);
    }
}