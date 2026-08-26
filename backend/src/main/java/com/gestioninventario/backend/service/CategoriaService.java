package com.gestioninventario.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gestioninventario.backend.entity.Categoria;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository){
        this.repository = repository;
    }

    public List<Categoria> listarCategorias(){
        return repository.findAll();
    }

    public Categoria listarCategoriaPorId(Long id_categoria){
        return repository.findById(id_categoria)
            .orElseThrow(() -> new RecursoNoEncontradoException("La categoria " + id_categoria + " no existe"));
    }

    public Categoria crearCategoria(Categoria categoria){
        LocalDateTime ahora = LocalDateTime.now();

        categoria.setFecha_creacion(ahora);
        categoria.setFecha_modificacion(ahora);

        return repository.save(categoria);
    }

    public Categoria actualizarCategoria(Long id_categoria, Categoria categoriaActualizada){
        Categoria categoria = repository.findById(id_categoria)
            .orElseThrow(() -> new RecursoNoEncontradoException("La categoria " + id_categoria + " no existe"));

        categoria.setCodigo_categoria(categoriaActualizada.getCodigo_categoria());
        categoria.setNombre_categoria(categoriaActualizada.getNombre_categoria());
        categoria.setDescripcion(categoriaActualizada.getDescripcion());
        categoria.setEstado(categoriaActualizada.getEstado());
        categoria.setFecha_modificacion(LocalDateTime.now());

        return repository.save(categoria);
    }

    public void eliminarCategoria(Long id_categoria) {

        Categoria categoria = repository.findById(id_categoria)
            .orElseThrow(() -> new RecursoNoEncontradoException("La categoria " + id_categoria + " no existe"));

        repository.delete(categoria);
    }
}