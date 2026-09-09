package com.gestioninventario.backend.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import com.gestioninventario.backend.application.dto.categoria.CategoriaCreateDTO;
import com.gestioninventario.backend.application.dto.categoria.CategoriaResponseDTO;
import com.gestioninventario.backend.application.dto.categoria.CategoriaUpdateDTO;
import com.gestioninventario.backend.domain.entity.Categoria;
import com.gestioninventario.backend.domain.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.domain.exception.EstadoSinCambiosException;
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

    private Pageable prepararPageable(Pageable pageable) {

        if (pageable.getSort().isUnsorted()) {
            return pageable;
        }

        Sort sort = Sort.unsorted();

        for (Sort.Order order : pageable.getSort()) {

            Sort nuevoOrden = JpaSort.unsafe(order.getDirection(), order.getProperty());

            sort = sort.and(nuevoOrden);
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    public Page<CategoriaResponseDTO> listarCategorias(Pageable pageable){
        Pageable pageableSeguro = prepararPageable(pageable);
        
        return repository.findAll(pageableSeguro).map(mapper::toResponseDTO);
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

    public CategoriaResponseDTO cambiarEstado(Long id_categoria,Categoria.Estado estado) {

    Categoria categoria = repository.findById(id_categoria)
        .orElseThrow(() -> new RecursoNoEncontradoException("La categoria " + id_categoria + " no existe"));

    if (categoria.getEstado() == estado) {
        String mensaje = switch (estado) {
            case ACTIVO -> "La categoria ya esta activa";
            case INACTIVO -> "La categoria ya esta inactiva";
        };

        throw new EstadoSinCambiosException(mensaje);
    }

    categoria.setEstado(estado);

    Categoria categoriaActualizada = repository.save(categoria);

    return mapper.toResponseDTO(categoriaActualizada);
}
}