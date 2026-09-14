package com.inventorymanagement.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventorymanagement.backend.domain.entity.Binnacle;

public interface BinnacleRepository extends JpaRepository<Binnacle, Long> {
}