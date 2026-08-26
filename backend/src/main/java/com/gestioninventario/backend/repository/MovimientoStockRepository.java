package com.gestioninventario.backend.repository;

import com.gestioninventario.backend.entity.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {
}