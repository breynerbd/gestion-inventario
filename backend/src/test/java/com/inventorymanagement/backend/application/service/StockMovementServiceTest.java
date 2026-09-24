package com.inventorymanagement.backend.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.inventorymanagement.backend.application.dto.movement.StockMovementCreateDTO;
import com.inventorymanagement.backend.application.dto.movement.StockMovementResponseDTO;
import com.inventorymanagement.backend.application.dto.movement.StockMovementUpdateDTO;
import com.inventorymanagement.backend.application.mapper.StockMovementMapper;
import com.inventorymanagement.backend.domain.entity.Product;
import com.inventorymanagement.backend.domain.entity.StockMovement;
import com.inventorymanagement.backend.domain.entity.User;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.infrastructure.persistence.repository.ProductRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.StockMovementRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class StockMovementServiceTest {

    @Mock
    private StockMovementRepository movementRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StockMovementMapper mapper;

    @InjectMocks
    private StockMovementService service;

    private StockMovement movement;
    private Product product;
    private User user;
    private StockMovementResponseDTO response;
    private StockMovementCreateDTO createDTO;
    private StockMovementUpdateDTO updateDTO;

    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2026, 9, 24, 8, 0, 0);

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1L);
        product.setProductCode("PROD001");
        product.setProductName("Coca Cola");
        product.setDescription("Bebida refrescante sabor cola de 600ml");
        product.setPurchasePrice(new BigDecimal("5.00"));
        product.setSalePrice(new BigDecimal("7.00"));
        product.setMinimumStock(10);
        product.setMaximumStock(80);
        product.setCurrentStock(50);
        product.setUnitOfMeasure(Product.UnitOfMeasure.UNIDAD);
        product.setStatus(Product.Status.ACTIVO);

        user = new User();
        user.setUserId(1L);
        user.setUsername("breyner");
        user.setEmail("breyner@gmail.com");
        user.setStatus(User.Status.ACTIVO);

        movement = new StockMovement();
        movement.setMovementId(1L);
        movement.setMovementType(StockMovement.MovementType.ENTRADA);
        movement.setQuantity(10);
        movement.setReferenceDocument("MOV001");
        movement.setReason("Compra de producto");
        movement.setMovementDate(DATE_TIME);
        movement.setProduct(product);
        movement.setUser(user);
        movement.setStatus(StockMovement.Status.ACTIVO);

        response = new StockMovementResponseDTO();
        response.setMovementId(1L);
        response.setMovementType(StockMovement.MovementType.ENTRADA);
        response.setProductId(1L);
        response.setProductName("Coca Cola");
        response.setQuantity(10);
        response.setReferenceDocument("MOV001");
        response.setReason("Compra de producto");
        response.setUserId(1L);
        response.setUsername("breyner");
        response.setStatus(StockMovement.Status.ACTIVO);

        createDTO = new StockMovementCreateDTO();
        createDTO.setMovementType(StockMovement.MovementType.ENTRADA);
        createDTO.setProductId(1L);
        createDTO.setQuantity(10);
        createDTO.setReferenceDocument("MOV001");
        createDTO.setReason("Compra de producto");

        updateDTO = new StockMovementUpdateDTO();
        updateDTO.setMovementType(StockMovement.MovementType.ENTRADA);
        updateDTO.setProductId(1L);
        updateDTO.setQuantity(15);
        updateDTO.setReferenceDocument("MOV002");
        updateDTO.setReason("Actualizacion de compra");
    }

    @Test
    void findAllMovements() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<StockMovement> movementPage = new PageImpl<>(List.of(movement));

        when(movementRepository.findAll(isA(Specification.class), isA(Pageable.class))).thenReturn(movementPage);

        when(mapper.toResponseDTO(movement)).thenReturn(response);

        Page<StockMovementResponseDTO> result = service.findAllMovements(
            null,
            null,
            null,
            null,
            null,
            pageable
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getMovementId());

        verify(mapper).toResponseDTO(movement);
    }

    @Test
    void findMovementById() {
        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
        when(mapper.toResponseDTO(movement)).thenReturn(response);

        StockMovementResponseDTO result = service.findMovementById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getMovementId());
        assertEquals(StockMovement.MovementType.ENTRADA, result.getMovementType());

        verify(movementRepository).findById(1L);
        verify(mapper).toResponseDTO(movement);
    }

    @Test
    void findMovementByIdReturnException() {
        when(movementRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findMovementById(10L));

        assertEquals("El movimiento 10 no existe", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void registerMovement() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByEmail("breyner@gmail.com")).thenReturn(Optional.of(user));
        when(mapper.toEntity(createDTO, product, user)).thenReturn(movement);
        when(productRepository.save(product)).thenReturn(product);
        when(movementRepository.save(movement)).thenReturn(movement);
        when(mapper.toResponseDTO(movement)).thenReturn(response);

        StockMovementResponseDTO result = service.registerMovement(createDTO, "breyner@gmail.com");

        assertNotNull(result);
        assertEquals(60, product.getCurrentStock());

        verify(productRepository).save(product);
        verify(movementRepository).save(movement);
    }

    @Test
    void registerMovementProductNotExist() {
        createDTO.setProductId(10L);

        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.registerMovement(createDTO, "breyner@gmail.com"));

        assertEquals("El producto 10 no existe", exception.getMessage());

        verifyNoInteractions(userRepository);
    }

    @Test
    void registerMovementUserNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByEmail("omar@gmail.com")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.registerMovement(createDTO, "omar@gmail.com"));

        assertEquals("El usuario logueado no existe", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void registerMovementInactiveProduct() {
        product.setStatus(Product.Status.INACTIVO);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByEmail("breyner@gmail.com")).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.registerMovement(createDTO, "breyner@gmail.com"));

        assertEquals("No se puede realizar el movimiento con un producto INACTIVO", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void registerMovementInactiveUser() {
        user.setStatus(User.Status.INACTIVO);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByEmail("breyner@gmail.com")).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.registerMovement(createDTO, "breyner@gmail.com"));

        assertEquals("No se puede realizar el movimiento con un usuario INACTIVO", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void registerMovementBlockedUser() {
        user.setStatus(User.Status.BLOQUEADO);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByEmail("breyner@gmail.com")).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.registerMovement(createDTO, "breyner@gmail.com"));

        assertEquals("No se puede realizar el movimiento con un usuario bloqueado", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test
    void registerMovementInvalidReason() {
        createDTO.setMovementType(StockMovement.MovementType.SALIDA);
        createDTO.setReason(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.registerMovement(createDTO, "breyner@gmail.com"));

        assertEquals("El motivo es obligatorio para este tipo de movimiento", exception.getMessage());

        verifyNoInteractions(productRepository);
    }

    @Test
    void registerExitMovement() {
        createDTO.setMovementType(StockMovement.MovementType.SALIDA);
        createDTO.setQuantity(10);
        createDTO.setReason("Producto vendido");

        movement.setMovementType(StockMovement.MovementType.SALIDA);
        movement.setQuantity(10);
        movement.setReason("Producto vendido");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByEmail("breyner@gmail.com")).thenReturn(Optional.of(user));
        when(mapper.toEntity(createDTO, product, user)).thenReturn(movement);
        when(productRepository.save(product)).thenReturn(product);
        when(movementRepository.save(movement)).thenReturn(movement);
        when(mapper.toResponseDTO(movement)).thenReturn(response);

        StockMovementResponseDTO result = service.registerMovement(createDTO, "breyner@gmail.com");

        assertNotNull(result);
        assertEquals(40, product.getCurrentStock());

        verify(productRepository).save(product);
    }

    @Test
    void registerMovementInvalidStock() {
        createDTO.setMovementType(StockMovement.MovementType.SALIDA);
        createDTO.setQuantity(70);
        createDTO.setReason("Venta de producto");

        movement.setMovementType(StockMovement.MovementType.SALIDA);
        movement.setQuantity(70);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByEmail("breyner@gmail.com")).thenReturn(Optional.of(user));
        when(mapper.toEntity(createDTO, product, user)).thenReturn(movement);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.registerMovement(createDTO, "breyner@gmail.com"));

        assertEquals("No hay suficiente stock para realizar el movimiento", exception.getMessage());
    }

    @Test
    void updateMovement() {
        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(movementRepository.save(movement)).thenReturn(movement);
        when(mapper.toResponseDTO(movement)).thenReturn(response);

        StockMovementResponseDTO result = service.updateMovement(1L, updateDTO);

        assertNotNull(result);
        assertEquals(55, product.getCurrentStock());

        verify(mapper).updateEntity(updateDTO, movement, product);
        verify(movementRepository).save(movement);
    }

    @Test
    void updateMovementNotExist() {
        when(movementRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updateMovement(10L, updateDTO));

        assertEquals("El movimiento 10 no existe", exception.getMessage());

        verifyNoInteractions(productRepository);
    }

    @Test
    void updateMovementProductNotExist() {
        updateDTO.setProductId(10L);

        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updateMovement(1L, updateDTO));

        assertEquals("El producto 10 no existe", exception.getMessage());
    }

    @Test
    void updateInactiveMovement() {
        movement.setStatus(StockMovement.Status.INACTIVO);

        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(movementRepository.save(movement)).thenReturn(movement);
        when(mapper.toResponseDTO(movement)).thenReturn(response);

        StockMovementResponseDTO result = service.updateMovement(1L, updateDTO);

        assertNotNull(result);
        assertEquals(50, product.getCurrentStock());

        verify(mapper).updateEntity(updateDTO, movement, product);
    }

    @Test
    void changeStatusInactive() {
        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
        when(productRepository.save(product)).thenReturn(product);
        when(movementRepository.save(movement)).thenReturn(movement);
        when(mapper.toResponseDTO(movement)).thenReturn(response);

        StockMovementResponseDTO result = service.changeStatus(1L, StockMovement.Status.INACTIVO);

        assertNotNull(result);
        assertEquals(40, product.getCurrentStock());
        assertEquals(StockMovement.Status.INACTIVO, movement.getStatus());

        verify(productRepository).save(product);
        verify(movementRepository).save(movement);
    }

    @Test
    void changeStatusActive() {
        movement.setStatus(StockMovement.Status.INACTIVO);

        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
        when(productRepository.save(product)).thenReturn(product);
        when(movementRepository.save(movement)).thenReturn(movement);
        when(mapper.toResponseDTO(movement)).thenReturn(response);

        StockMovementResponseDTO result = service.changeStatus(1L, StockMovement.Status.ACTIVO);

        assertNotNull(result);
        assertEquals(60, product.getCurrentStock());
        assertEquals(StockMovement.Status.ACTIVO, movement.getStatus());

        verify(productRepository).save(product);
        verify(movementRepository).save(movement);
    }

    @Test
    void changeStatusInactiveExit() {
        movement.setMovementType(StockMovement.MovementType.SALIDA);
        movement.setQuantity(10);

        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
        when(productRepository.save(product)).thenReturn(product);
        when(movementRepository.save(movement)).thenReturn(movement);
        when(mapper.toResponseDTO(movement)).thenReturn(response);

        StockMovementResponseDTO result = service.changeStatus(1L, StockMovement.Status.INACTIVO);

        assertNotNull(result);
        assertEquals(60, product.getCurrentStock());
        assertEquals(StockMovement.Status.INACTIVO, movement.getStatus());
    }

    @Test
    void changeStatusAlreadyActive() {
        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, StockMovement.Status.ACTIVO));

        assertEquals("El movimiento ya esta ACTIVO", exception.getMessage());
    }

    @Test
    void changeStatusAlreadyInactive() {
        movement.setStatus(StockMovement.Status.INACTIVO);

        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, StockMovement.Status.INACTIVO));

        assertEquals("El movimiento ya esta INACTIVO", exception.getMessage());
    }

    @Test
    void changeStatusInactiveWithoutStock() {
        product.setCurrentStock(5);

        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.changeStatus(1L, StockMovement.Status.INACTIVO));

        assertEquals("No es posible inactivar el movimiento porque el stock quedaria negativo", exception.getMessage());
    }

    @Test
    void changeStatusMovementNotExist() {
        when(movementRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.changeStatus(10L, StockMovement.Status.INACTIVO));

        assertEquals("El movimiento 10 no existe", exception.getMessage());

        verify(movementRepository).findById(10L);
        verifyNoInteractions(productRepository);
    }
}