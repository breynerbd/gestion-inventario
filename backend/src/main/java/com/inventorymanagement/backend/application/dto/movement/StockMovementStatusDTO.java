package com.inventorymanagement.backend.application.dto.movement;

import com.inventorymanagement.backend.domain.entity.StockMovement.Status;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockMovementStatusDTO {

    @NotNull(message = "El estado es obligatorio")
    private Status status;
}