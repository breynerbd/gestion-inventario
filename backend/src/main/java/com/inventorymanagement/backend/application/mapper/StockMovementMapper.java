package com.inventorymanagement.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.inventorymanagement.backend.domain.entity.StockMovement;
import com.inventorymanagement.backend.application.dto.movement.StockMovementCreateDTO;
import com.inventorymanagement.backend.application.dto.movement.StockMovementResponseDTO;
import com.inventorymanagement.backend.application.dto.movement.StockMovementUpdateDTO;
import com.inventorymanagement.backend.domain.entity.Product;
import com.inventorymanagement.backend.domain.entity.User;

@Component
public class StockMovementMapper {

    public StockMovement toEntity(StockMovementCreateDTO dto, Product product, User user) {

        StockMovement movement = new StockMovement();

        movement.setMovementType(dto.getMovementType());
        movement.setQuantity(dto.getQuantity());
        movement.setReferenceDocument(dto.getReferenceDocument());
        movement.setReason(dto.getReason());
        movement.setProduct(product);
        movement.setUser(user);
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
        dto.setModificationDate(movement.getModificationDate());

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