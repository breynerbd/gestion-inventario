package com.gestioninventario.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestioninventario.backend.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
}