package com.gestioninventario.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestioninventario.backend.domain.entity.Binnacle;

public interface BinnacleRepository extends JpaRepository<Binnacle, Long> {
}