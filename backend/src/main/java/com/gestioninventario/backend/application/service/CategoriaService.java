package com.gestioninventario.backend.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gestioninventario.backend.application.dto.categoria.CategoriaCreateDTO;
import com.gestioninventario.backend.application.dto.categoria.CategoriaResponseDTO;
import com.gestioninventario.backend.application.dto.categoria.CategoriaUpdateDTO;
import com.gestioninventario.backend.domain.entity.Categoria;
import com.gestioninventario.backend.domain.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.application.mapper.CategoriaMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.CategoriaRepository;;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;

    public CategoriaService(CategoriaRepository repository, CategoriaMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CategoriaResponseDTO> listarCategorias(){
        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public CategoriaResponseDTO listarCategoriaPorId(Long id_categoria){
        Categoria categoria = repository.findById(id_categoria)
            .orElseThrow(() -> new RecursoNoEncontradoException("La categoria " + id_categoria + " no existe"));

        return mapper.toResponseDTO(categoria);
    }

    public CategoriaResponseDTO crearCategoria(CategoriaCreateDTO categoriaDTO){
        Categoria categoria = mapper.toEntity(categoriaDTO);

        Categoria categoriaGuardada = repository.save(categoria); 
        return mapper.toResponseDTO(categoriaGuardada);
    }

    public CategoriaResponseDTO actualizarCategoria(Long id_categoria, CategoriaUpdateDTO categoriaDTO){
        Categoria categoria = repository.findById(id_categoria)
            .orElseThrow(() -> new RecursoNoEncontradoException("La categoria " + id_categoria + " no existe"));

        mapper.updateEntity(categoriaDTO, categoria);

        Categoria categoriaActualizada = repository.save(categoria);

        return mapper.toResponseDTO(categoriaActualizada);
    }

    public void eliminarCategoria(Long id_categoria) {

        Categoria categoria = repository.findById(id_categoria)
            .orElseThrow(() -> new RecursoNoEncontradoException("La categoria " + id_categoria + " no existe"));

        repository.delete(categoria);
    }
}