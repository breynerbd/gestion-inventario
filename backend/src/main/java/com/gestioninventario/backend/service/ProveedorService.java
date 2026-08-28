package com.gestioninventario.backend.service;

import com.gestioninventario.backend.dto.proveedor.ProveedorCreateDTO;
import com.gestioninventario.backend.dto.proveedor.ProveedorResponseDTO;
import com.gestioninventario.backend.dto.proveedor.ProveedorUpdateDTO;
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

    public List<ProveedorResponseDTO> listarProveedores() {
        return repository.findAll().stream().map(this::proveedorResponse).toList();
    }

    public ProveedorResponseDTO obtenerProveedor(Long id_proveedor) {

        Proveedor proveedor = repository.findById(id_proveedor)
            .orElseThrow(() -> new RecursoNoEncontradoException("El proveedor " + id_proveedor + " no existe"));

        return proveedorResponse(proveedor);
    }

    public ProveedorResponseDTO crearProveedor(ProveedorCreateDTO proveedorDto) {
        Proveedor proveedor = new Proveedor();
        
        proveedor.setCodigo_proveedor(proveedorDto.getCodigo_proveedor()); 
        proveedor.setTipo_documento(proveedorDto.getTipo_documento()); 
        proveedor.setNumero_documento(proveedorDto.getNumero_documento()); 
        proveedor.setRazon_social(proveedorDto.getRazon_social()); 
        proveedor.setNombre_contacto(proveedorDto.getNombre_contacto()); 
        proveedor.setTelefono(proveedorDto.getTelefono()); 
        proveedor.setCorreo_electronico(proveedorDto.getCorreo_electronico()); 
        proveedor.setDireccion(proveedorDto.getDireccion()); 
        proveedor.setEstado(Proveedor.Estado.ACTIVO);
        
        Proveedor proveedorGuardado = repository.save(proveedor); 
        
        return proveedorResponse(proveedorGuardado);
    }

    public ProveedorResponseDTO actualizarProveedor(Long id_proveedor, ProveedorUpdateDTO proveedorDto) {

        Proveedor proveedor = repository.findById(id_proveedor)
            .orElseThrow(() -> new RecursoNoEncontradoException("El proveedor " + id_proveedor + " no existe"));

        proveedor.setTipo_documento(proveedorDto.getTipo_documento());
        proveedor.setNumero_documento(proveedorDto.getNumero_documento());
        proveedor.setRazon_social(proveedorDto.getRazon_social());
        proveedor.setNombre_contacto(proveedorDto.getNombre_contacto());
        proveedor.setTelefono(proveedorDto.getTelefono());
        proveedor.setCorreo_electronico(proveedorDto.getCorreo_electronico());
        proveedor.setDireccion(proveedorDto.getDireccion());

        Proveedor proveedorActualizado = repository.save(proveedor);

        return proveedorResponse(proveedorActualizado);
    }

    public void eliminarProveedor(Long id_proveedor) {

        Proveedor proveedor = repository.findById(id_proveedor)
            .orElseThrow(() -> new RecursoNoEncontradoException("El proveedor " + id_proveedor + " no existe"));

        repository.delete(proveedor);
    }

    private ProveedorResponseDTO proveedorResponse(Proveedor proveedor) { 
        ProveedorResponseDTO dto = new ProveedorResponseDTO(); 
        
        dto.setId_proveedor(proveedor.getId_proveedor()); 
        dto.setCodigo_proveedor(proveedor.getCodigo_proveedor()); 
        dto.setTipo_documento(proveedor.getTipo_documento()); 
        dto.setNumero_documento(proveedor.getNumero_documento()); 
        dto.setRazon_social(proveedor.getRazon_social()); 
        dto.setNombre_contacto(proveedor.getNombre_contacto()); 
        dto.setTelefono(proveedor.getTelefono()); 
        dto.setCorreo_electronico(proveedor.getCorreo_electronico()); 
        dto.setDireccion(proveedor.getDireccion()); 
        dto.setEstado(proveedor.getEstado()); 
        
        return dto; }
}