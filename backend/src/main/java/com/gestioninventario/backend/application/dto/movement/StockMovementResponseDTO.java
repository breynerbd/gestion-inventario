package com.gestioninventario.backend.application.dto.movement;

import java.time.LocalDateTime;

import com.gestioninventario.backend.domain.entity.StockMovement.MovementType;
import com.gestioninventario.backend.domain.entity.StockMovement.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockMovementResponseDTO {

    private Long movementId;
    private MovementType movementType;
    private Long productId;
    private String productName;
    private Integer quantity;
    private String referenceDocument;
    private String reason;
    private Long userId;
    private String username;
    private LocalDateTime movementDate;
    private Status status;
}