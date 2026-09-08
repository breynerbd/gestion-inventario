package com.gestioninventario.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestioninventario.backend.domain.entity.Bitacora;

public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {
}