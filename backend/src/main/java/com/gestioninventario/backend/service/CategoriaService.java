package com.gestioninventario.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gestioninventario.backend.dto.categoria.CategoriaCreateDTO;
import com.gestioninventario.backend.dto.categoria.CategoriaResponseDTO;
import com.gestioninventario.backend.dto.categoria.CategoriaUpdateDTO;
import com.gestioninventario.backend.entity.Categoria;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository){
        this.repository = repository;
    }

    public List<CategoriaResponseDTO> listarCategorias(){
        return repository.findAll().stream().map(this::categoriaResponse).toList();
    }

    public CategoriaResponseDTO listarCategoriaPorId(Long id_categoria){
        Categoria categoria = repository.findById(id_categoria)
            .orElseThrow(() -> new RecursoNoEncontradoException("La categoria " + id_categoria + " no existe"));

        return categoriaResponse(categoria);
    }

    public CategoriaResponseDTO crearCategoria(CategoriaCreateDTO categoriaDTO){
        Categoria categoria = new Categoria();

        categoria.setCodigo_categoria(categoriaDTO.getCodigo_categoria()); 
        categoria.setNombre_categoria(categoriaDTO.getNombre_categoria()); 
        categoria.setDescripcion(categoriaDTO.getDescripcion()); 
        categoria.setEstado(Categoria.Estado.ACTIVO); 
        LocalDateTime ahora = LocalDateTime.now(); 
        categoria.setFecha_creacion(ahora); 
        categoria.setFecha_modificacion(ahora); 

        Categoria categoriaGuardada = repository.save(categoria); 
        return categoriaResponse(categoriaGuardada);
    }

    public CategoriaResponseDTO actualizarCategoria(Long id_categoria, CategoriaUpdateDTO categoriaDTO){
        Categoria categoria = repository.findById(id_categoria)
            .orElseThrow(() -> new RecursoNoEncontradoException("La categoria " + id_categoria + " no existe"));

        categoria.setNombre_categoria(categoriaDTO.getNombre_categoria());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        categoria.setFecha_modificacion(LocalDateTime.now());

        Categoria categoriaActualizada = repository.save(categoria);

        return categoriaResponse(categoriaActualizada);
    }

    public void eliminarCategoria(Long id_categoria) {

        Categoria categoria = repository.findById(id_categoria)
            .orElseThrow(() -> new RecursoNoEncontradoException("La categoria " + id_categoria + " no existe"));

        repository.delete(categoria);
    }

    private CategoriaResponseDTO categoriaResponse(Categoria categoria){
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