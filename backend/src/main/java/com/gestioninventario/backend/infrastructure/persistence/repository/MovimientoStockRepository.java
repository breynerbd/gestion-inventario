package com.gestioninventario.backend.infrastructure.persistence.repository;

import com.gestioninventario.backend.domain.entity.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {
}