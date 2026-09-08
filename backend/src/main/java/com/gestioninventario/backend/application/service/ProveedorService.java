package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.application.dto.proveedor.ProveedorCreateDTO;
import com.gestioninventario.backend.application.dto.proveedor.ProveedorResponseDTO;
import com.gestioninventario.backend.application.dto.proveedor.ProveedorUpdateDTO;
import com.gestioninventario.backend.domain.entity.Proveedor;
import com.gestioninventario.backend.domain.exception.EstadoSinCambiosException;
import com.gestioninventario.backend.domain.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.application.mapper.ProveedorMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.ProveedorRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProveedorService {

    private final ProveedorRepository repository;
    private final ProveedorMapper mapper;

    public ProveedorService(ProveedorRepository repository, ProveedorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<ProveedorResponseDTO> listarProveedores(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponseDTO);
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

    public ProveedorResponseDTO cambiarEstado(Long id_proveedor, Proveedor.Estado estado) {

        Proveedor proveedor = repository.findById(id_proveedor)
            .orElseThrow(() ->new RecursoNoEncontradoException("El proveedor " + id_proveedor + " no existe"));

        if (proveedor.getEstado() == estado) {
            String mensaje = switch (estado) {
                case ACTIVO -> "El proveedor ya esta activo";
                case INACTIVO -> "El proveedor ya esta inactivo";
            };

            throw new EstadoSinCambiosException(mensaje);
        }

        proveedor.setEstado(estado);

        Proveedor proveedorActualizado = repository.save(proveedor);

        return mapper.toResponseDTO(proveedorActualizado);
    }
}