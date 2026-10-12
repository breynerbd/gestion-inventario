package com.inventorymanagement.backend.application.dto.movement;

import java.time.LocalDate;

public record StockMovementSummaryDTO(LocalDate date, Long entries,Long exits) {
}