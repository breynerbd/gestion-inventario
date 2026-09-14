package com.inventorymanagement.backend.infrastructure.persistence.repository;

import com.inventorymanagement.backend.domain.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}