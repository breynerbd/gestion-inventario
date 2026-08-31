package com.gestioninventario.backend.infrastructure.persistence.repository;

import com.gestioninventario.backend.domain.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
}