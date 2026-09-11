package com.gestioninventario.backend.application.dto.movement;

import com.gestioninventario.backend.domain.entity.StockMovement.MovementType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockMovementCreateDTO {

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private MovementType movementType;

    @NotNull(message = "El producto es obligatorio")
    @Positive(message = "El identificador del producto debe ser mayor que 0")
    private Long productId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor que 0")
    private Integer quantity;

    @Size(max = 30, message = "El documento de referencia no puede superar los 30 caracteres")
    private String referenceDocument;

    @Size(max = 250, message = "El motivo no puede superar los 250 caracteres")
    private String reason;
}