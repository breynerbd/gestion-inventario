package com.inventorymanagement.backend.infrastructure.persistence.repository;

import com.inventorymanagement.backend.domain.entity.StockMovement;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long>, JpaSpecificationExecutor<StockMovement> {
    @Query("select m from StockMovement m where m.status = :status order by m.movementDate")
    List<StockMovement> findActiveMovements(@Param("status") StockMovement.Status status);

    @Query("""
        select coalesce(sum((p.salePrice - p.purchasePrice) * m.quantity), 0) 
        from StockMovement m 
        join m.product p 
        where m.user.userId = :userId 
        and m.movementType = :movementType 
        and m.status = :status""")
    BigDecimal calculateMovementsValueByUser(
        @Param("userId") Long userId, @Param("movementType") StockMovement.MovementType movementType,
        @Param("status") StockMovement.Status status
    );

    @Query("select coalesce(sum(quantity), 0) from StockMovement where user.userId = :userId and movementType = :movementType and status = :status")
    Long countProductsByMovement(
        @Param("userId") Long userId, @Param("movementType") StockMovement.MovementType movementType,
        @Param("status") StockMovement.Status status
    );
}