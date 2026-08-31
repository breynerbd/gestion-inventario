package com.gestioninventario.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.application.dto.proveedor.ProveedorCreateDTO;
import com.gestioninventario.backend.application.dto.proveedor.ProveedorResponseDTO;
import com.gestioninventario.backend.application.dto.proveedor.ProveedorUpdateDTO;
import com.gestioninventario.backend.domain.entity.Proveedor;

@Component
public class ProveedorMapper {

    public Proveedor toEntity(ProveedorCreateDTO dto) {

        Proveedor proveedor = new Proveedor();

        proveedor.setCodigo_proveedor(dto.getCodigo_proveedor());
        proveedor.setTipo_documento(dto.getTipo_documento());
        proveedor.setNumero_documento(dto.getNumero_documento());
        proveedor.setRazon_social(dto.getRazon_social());
        proveedor.setNombre_contacto(dto.getNombre_contacto());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setCorreo_electronico(dto.getCorreo_electronico());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setEstado(Proveedor.Estado.ACTIVO);

        return proveedor;
    }

    public void updateEntity(ProveedorUpdateDTO dto, Proveedor proveedor) {

        proveedor.setTipo_documento(dto.getTipo_documento());
        proveedor.setNumero_documento(dto.getNumero_documento());
        proveedor.setRazon_social(dto.getRazon_social());
        proveedor.setNombre_contacto(dto.getNombre_contacto());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setCorreo_electronico(dto.getCorreo_electronico());
        proveedor.setDireccion(dto.getDireccion());
    }

    public ProveedorResponseDTO toResponseDTO(Proveedor proveedor) {

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

        return dto;
    }
}
