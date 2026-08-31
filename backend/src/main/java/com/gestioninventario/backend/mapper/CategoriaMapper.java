package com.gestioninventario.backend.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.dto.categoria.CategoriaCreateDTO;
import com.gestioninventario.backend.dto.categoria.CategoriaResponseDTO;
import com.gestioninventario.backend.dto.categoria.CategoriaUpdateDTO;
import com.gestioninventario.backend.entity.Categoria;

@Component
public class CategoriaMapper {

    public Categoria toEntity(CategoriaCreateDTO dto) {
        Categoria categoria = new Categoria();

        categoria.setCodigo_categoria(dto.getCodigo_categoria());
        categoria.setNombre_categoria(dto.getNombre_categoria());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setEstado(Categoria.Estado.ACTIVO); 
        LocalDateTime ahora = LocalDateTime.now(); 
        categoria.setFecha_creacion(ahora); 
        categoria.setFecha_modificacion(ahora); 

        return categoria;
    }

    public void updateEntity(CategoriaUpdateDTO dto, Categoria categoria) {
        categoria.setNombre_categoria(dto.getNombre_categoria());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setFecha_modificacion(LocalDateTime.now());
    }

    public CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();

        dto.setId_categoria(categoria.getId_categoria());
        dto.setCodigo_categoria(categoria.getCodigo_categoria());
        dto.setNombre_categoria(categoria.getNombre_categoria());
        dto.setDescripcion(categoria.getDescripcion());
        dto.setEstado(categoria.getEstado());
        dto.setFecha_creacion(categoria.getFecha_creacion());
        dto.setFecha_modificacion(categoria.getFecha_modificacion());

        return dto;
    }
}