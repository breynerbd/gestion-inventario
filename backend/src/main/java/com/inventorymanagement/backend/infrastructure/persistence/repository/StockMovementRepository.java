package com.inventorymanagement.backend.infrastructure.persistence.repository;

import com.inventorymanagement.backend.domain.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long>, JpaSpecificationExecutor<StockMovement> {
}