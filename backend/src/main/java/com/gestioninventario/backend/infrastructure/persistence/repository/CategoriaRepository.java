package com.gestioninventario.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestioninventario.backend.domain.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
}