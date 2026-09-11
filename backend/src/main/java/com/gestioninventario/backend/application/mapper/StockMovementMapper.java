package com.gestioninventario.backend.application.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.domain.entity.StockMovement;
import com.gestioninventario.backend.application.dto.movement.StockMovementCreateDTO;
import com.gestioninventario.backend.application.dto.movement.StockMovementResponseDTO;
import com.gestioninventario.backend.application.dto.movement.StockMovementUpdateDTO;
import com.gestioninventario.backend.domain.entity.Product;
import com.gestioninventario.backend.domain.entity.User;

@Component
public class StockMovementMapper {
    private static final ZoneId ZONE_ID = ZoneId.of("America/Guatemala");

    public StockMovement toEntity(StockMovementCreateDTO dto, Product product, User user) {

        StockMovement movement = new StockMovement();

        movement.setMovementType(dto.getMovementType());
        movement.setQuantity(dto.getQuantity());
        movement.setReferenceDocument(dto.getReferenceDocument());
        movement.setReason(dto.getReason());
        movement.setProduct(product);
        movement.setUser(user);
        movement.setMovementDate(LocalDateTime.now(ZONE_ID));
        movement.setStatus(StockMovement.Status.ACTIVO);

        return movement;
    }

    public void updateEntity(StockMovementUpdateDTO dto, StockMovement movement, Product product) {

        movement.setMovementType(dto.getMovementType());
        movement.setQuantity(dto.getQuantity());
        movement.setReferenceDocument(dto.getReferenceDocument());
        movement.setReason(dto.getReason());     
        movement.setProduct(product);
    }

    public StockMovementResponseDTO toResponseDTO(
            StockMovement movement) {

        StockMovementResponseDTO dto = new StockMovementResponseDTO();

        dto.setMovementId(movement.getMovementId());
        dto.setMovementType(movement.getMovementType());
        dto.setQuantity(movement.getQuantity());
        dto.setReferenceDocument(movement.getReferenceDocument());
        dto.setReason(movement.getReason());
        dto.setMovementDate(movement.getMovementDate());
        dto.setStatus(movement.getStatus());

        if (movement.getProduct() != null) {
            dto.setProductId(
                    movement.getProduct().getProductId()
            );

            dto.setProductName(
                    movement.getProduct().getProductName()
            );
        }

        if (movement.getUser() != null) {
            dto.setUserId(
                    movement.getUser().getUserId()
            );

            dto.setUsername(
                    movement.getUser().getUsername()
            );
        }

        return dto;
    }
}