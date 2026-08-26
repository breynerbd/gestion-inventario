package com.gestioninventario.backend.repository;

import com.gestioninventario.backend.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
}