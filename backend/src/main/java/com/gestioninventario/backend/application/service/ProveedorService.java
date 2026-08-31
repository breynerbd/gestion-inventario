package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.application.dto.proveedor.ProveedorCreateDTO;
import com.gestioninventario.backend.application.dto.proveedor.ProveedorResponseDTO;
import com.gestioninventario.backend.application.dto.proveedor.ProveedorUpdateDTO;
import com.gestioninventario.backend.domain.entity.Proveedor;
import com.gestioninventario.backend.domain.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.application.mapper.ProveedorMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository repository;
    private final ProveedorMapper mapper;

    public ProveedorService(ProveedorRepository repository, ProveedorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ProveedorResponseDTO> listarProveedores() {
        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public ProveedorResponseDTO obtenerProveedor(Long id_proveedor) {

        Proveedor proveedor = repository.findById(id_proveedor)
            .orElseThrow(() -> new RecursoNoEncontradoException("El proveedor " + id_proveedor + " no existe"));

        return mapper.toResponseDTO(proveedor);
    }

    public ProveedorResponseDTO crearProveedor(ProveedorCreateDTO proveedorDto) {
        Proveedor proveedor = mapper.toEntity(proveedorDto);
        
        Proveedor proveedorGuardado = repository.save(proveedor); 
        
        return mapper.toResponseDTO(proveedorGuardado);
    }

    public ProveedorResponseDTO actualizarProveedor(Long id_proveedor, ProveedorUpdateDTO proveedorDto) {

        Proveedor proveedor = repository.findById(id_proveedor)
            .orElseThrow(() -> new RecursoNoEncontradoException("El proveedor " + id_proveedor + " no existe"));
        
        mapper.updateEntity(proveedorDto, proveedor);

        Proveedor proveedorActualizado = repository.save(proveedor);

        return mapper.toResponseDTO(proveedorActualizado);
    }

    public void eliminarProveedor(Long id_proveedor) {

        Proveedor proveedor = repository.findById(id_proveedor)
            .orElseThrow(() -> new RecursoNoEncontradoException("El proveedor " + id_proveedor + " no existe"));

        repository.delete(proveedor);
    }
}