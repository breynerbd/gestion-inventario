package com.inventorymanagement.backend.application.service;

import com.inventorymanagement.backend.domain.entity.StockMovement;
import com.inventorymanagement.backend.domain.entity.Product;
import com.inventorymanagement.backend.domain.entity.User;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.application.dto.movement.StockMovementCreateDTO;
import com.inventorymanagement.backend.application.dto.movement.StockMovementResponseDTO;
import com.inventorymanagement.backend.application.dto.movement.StockMovementUpdateDTO;
import com.inventorymanagement.backend.application.mapper.StockMovementMapper;
import com.inventorymanagement.backend.infrastructure.persistence.repository.StockMovementRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.ProductRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor 
public class StockMovementService {

    private final StockMovementRepository movementRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final StockMovementMapper mapper;
    private static final String MOVEMENT_NOT_FOUND = "El movimiento ";
    private static final String NOT_EXIST = " no existe";

    private static final String LOGGER_NOT_FOUND = "No se encontro el movimiento: {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(StockMovementService.class);

    private void validateReason(StockMovement.MovementType movementType, String reason) {
        LOGGER.debug("Validando motivo del movimiento");

        if ((movementType == StockMovement.MovementType.SALIDA
                || movementType == StockMovement.MovementType.AJUSTE_POSITIVO
                || movementType == StockMovement.MovementType.AJUSTE_NEGATIVO)
                && (reason == null || reason.isBlank())) {

            throw new IllegalArgumentException("El motivo es obligatorio para este tipo de movimiento");
        }
    }

    private void validateActiveEntities(Product product, User user) {
        LOGGER.debug("Validando producto y usuario activos");

        if (product.getStatus() == Product.Status.INACTIVO) {
            LOGGER.warn("El producto {} esta inactivo", product.getProductId());
            throw new IllegalArgumentException("No se puede realizar el movimiento con un producto INACTIVO");
        }

        if (user.getStatus() == User.Status.INACTIVO) {
            LOGGER.warn("El usuario {} esta inactivo", user.getUserId());
            throw new IllegalArgumentException("No se puede realizar el movimiento con un usuario INACTIVO");
        }

        if (user.getStatus() == User.Status.BLOQUEADO) {
            LOGGER.warn("El usuario {} esta bloqueado", user.getUserId());
            throw new IllegalArgumentException("No se puede realizar el movimiento con un usuario bloqueado");
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        LOGGER.debug("Validando rango de fechas");

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            LOGGER.warn("El rango de fechas no es valido");
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha final");
        }
    }

    public Page<StockMovementResponseDTO> findAllMovements(Long productId, StockMovement.MovementType movementType, LocalDate startDate, 
            LocalDate endDate, Long userId, Pageable pageable) {

        LOGGER.debug("Obteniendo datos de movimientos existentes");

        validateDateRange(startDate, endDate);

        Specification<StockMovement> specification = Specification.unrestricted();

        if (productId != null) {
            specification = specification.and((root, query, cb) -> 
                cb.equal(root.get("product").get("productId"), productId));
        }

        if (movementType != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("movementType"), movementType));
        }

        if (startDate != null) {
            specification = specification.and((root, query, cb) -> 
                cb.greaterThanOrEqualTo(root.get("startDate"),startDate.atStartOfDay()));
        }

        if (endDate != null) {
            specification = specification.and((root, query, cb) ->
                cb.lessThan(root.get("endDate"),endDate.plusDays(1).atStartOfDay()));
        }

        if (userId != null) {
            specification = specification.and((root, query, cb) ->
                cb.equal(root.get("user").get("userId"), userId));
        }

        return movementRepository.findAll(specification, pageable).map(mapper::toResponseDTO);
    }

    public StockMovementResponseDTO findMovementById(Long movementId) {
        LOGGER.debug("Buscando movimiento: {}", movementId);

        StockMovement movement = movementRepository.findById(movementId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, movementId);
                return new ResourceNotFoundException(MOVEMENT_NOT_FOUND + movementId + NOT_EXIST);}
            );

        return mapper.toResponseDTO(movement);
    }

    private void applyMovement(StockMovement movement, Product product) {
        LOGGER.debug("Aplicando movimiento {} al producto {}", movement.getMovementType(), product.getProductId());

        switch (movement.getMovementType()) {
            case ENTRADA, AJUSTE_POSITIVO -> product.setCurrentStock(product.getCurrentStock() + movement.getQuantity());

            case SALIDA, AJUSTE_NEGATIVO -> {
                int newStock = product.getCurrentStock() - movement.getQuantity();

                if (newStock < 0) {
                    throw new IllegalArgumentException("No hay suficiente stock para realizar el movimiento");
                }

                product.setCurrentStock(newStock);
            }
        }
    }

    private void revertMovement(StockMovement movement, Product product) {
        LOGGER.debug("Revirtiendo movimiento {} del producto {}", movement.getMovementType(), product.getProductId());

        switch (movement.getMovementType()) {
            case ENTRADA, AJUSTE_POSITIVO -> {
                int newStock = product.getCurrentStock() - movement.getQuantity();

                if (newStock < 0) {
                    throw new IllegalArgumentException("No es posible inactivar el movimiento porque el stock quedaria negativo");
                }

                product.setCurrentStock(newStock);
            }

            case SALIDA, AJUSTE_NEGATIVO -> product.setCurrentStock(product.getCurrentStock() + movement.getQuantity());
        }
    }

    @Transactional
    public StockMovementResponseDTO registerMovement(StockMovementCreateDTO movementDto, String userEmail) {
        LOGGER.debug("Registrando un movimiento de stock");

        validateReason(movementDto.getMovementType(), movementDto.getReason());

        Product product = productRepository.findById(movementDto.getProductId())
            .orElseThrow(() -> {
                LOGGER.warn("No se encontro el producto: {}", movementDto.getProductId());
                return new ResourceNotFoundException("El producto " + movementDto.getProductId() + NOT_EXIST);
            });

        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> {
                LOGGER.warn("No se encontro el usuario logueado");
                return new ResourceNotFoundException("El usuario logueado no existe");
            });

        validateActiveEntities(product, user);

        StockMovement movement = mapper.toEntity(movementDto, product, user);

        applyMovement(movement, product);

        productRepository.save(product);

        StockMovement savedMovement = movementRepository.save(movement);

        LOGGER.info("Se registro el movimiento con id: {}", savedMovement.getMovementId());

        return mapper.toResponseDTO(savedMovement);
    }

    @Transactional
    public StockMovementResponseDTO updateMovement(Long movementId, StockMovementUpdateDTO movementDto) {
        LOGGER.debug("Actualizando movimiento: {}", movementId);

        validateReason(movementDto.getMovementType(), movementDto.getReason());

        StockMovement movement = movementRepository.findById(movementId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, movementId);
                return new ResourceNotFoundException(MOVEMENT_NOT_FOUND + movementId + NOT_EXIST);}
            );

        Product previousProduct = movement.getProduct();

        Product newProduct = productRepository.findById(movementDto.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("El producto " + movementDto.getProductId() + NOT_EXIST));

        validateActiveEntities(newProduct, movement.getUser());

        if (movement.getStatus() == StockMovement.Status.ACTIVO) {

            revertMovement(movement, previousProduct);

            StockMovement movementNew = new StockMovement();
            movementNew.setMovementType(movementDto.getMovementType());
            movementNew.setQuantity(movementDto.getQuantity());

            if (previousProduct.getProductId().equals(newProduct.getProductId())) {

                applyMovement(movementNew, previousProduct);

                productRepository.save(previousProduct);

            } else {

                applyMovement(movementNew, newProduct);

                productRepository.save(previousProduct);
                productRepository.save(newProduct);
            }
        }

        mapper.updateEntity(movementDto, movement, newProduct);

        StockMovement updatedMovement = movementRepository.save(movement);

        LOGGER.info("El movimiento {} se ha actualizado", movementId);

        return mapper.toResponseDTO(updatedMovement);
    }

    @Transactional
    public StockMovementResponseDTO changeStatus(Long movementId, StockMovement.Status status) {
        LOGGER.debug("Cambiando estado del movimiento {} a {}", movementId, status);

        StockMovement movement = movementRepository.findById(movementId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, movementId);
                return new ResourceNotFoundException(MOVEMENT_NOT_FOUND + movementId + NOT_EXIST);
            });

        if (movement.getStatus() == status) {
            String message = switch (status) {
                case ACTIVO -> "El movimiento ya esta ACTIVO";
                case INACTIVO -> "El movimiento ya esta INACTIVO";
            };

            throw new StatusUnchangedException(message);
        }

        Product product = movement.getProduct();

        if (status == StockMovement.Status.INACTIVO) {
            revertMovement(movement, product);
        } else {
            validateActiveEntities(product, movement.getUser());
            applyMovement(movement, product);
        }

        movement.setStatus(status);

        productRepository.save(product);

        StockMovement updatedMovement = movementRepository.save(movement);

        LOGGER.info("El estado del movimiento {} se ha cambiado a {}", movementId, status);

        return mapper.toResponseDTO(updatedMovement);
    }
}