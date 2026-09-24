package com.inventorymanagement.backend.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inventorymanagement.backend.application.dto.product.ProductCreateDTO;
import com.inventorymanagement.backend.application.dto.product.ProductResponseDTO;
import com.inventorymanagement.backend.application.dto.product.ProductUpdateDTO;
import com.inventorymanagement.backend.application.mapper.ProductMapper;
import com.inventorymanagement.backend.domain.entity.Category;
import com.inventorymanagement.backend.domain.entity.Product;
import com.inventorymanagement.backend.domain.entity.Supplier;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.infrastructure.persistence.repository.CategoryRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.ProductRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.SupplierRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock 
    private ProductRepository repository;

    @Mock 
    private CategoryRepository categoryRepository;

    @Mock 
    private SupplierRepository supplierRepository;

    @Mock 
    private ProductMapper mapper;

    @InjectMocks 
    private ProductService service;

    private Product product;
    private Category category;
    private Supplier supplier;
    private ProductResponseDTO response;
    private ProductCreateDTO productDTO;
    private ProductUpdateDTO update;

    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2026, 9, 24, 8, 0, 0);

    @BeforeEach 
    void setUp(){
        category = new Category();
        category.setCategoryId(1L);
        category.setCategoryCode("CAT001");
        category.setCategoryName("Bebidas Gaseosas");
        category.setDescription("Bebidas frias con gas");
        category.setCreationDate(DATE_TIME);
        category.setModificationDate(DATE_TIME);
        category.setStatus(Category.Status.ACTIVO);

        supplier = new Supplier();
        supplier.setSupplierId(1L);
        supplier.setSupplierCode("PROV001");
        supplier.setBusinessName("Distribuidora");
        supplier.setContactName("Carlos Lopez");
        supplier.setPhone("85967412");
        supplier.setEmail("proveedor@gmail.com");
        supplier.setAddress("Ciudad de Guatemala");
        supplier.setStatus(Supplier.Status.ACTIVO);

        product = new Product();
        product.setProductId(1L);
        product.setProductCode("PROD001");
        product.setProductName("Coca Cola");
        product.setDescription("Bebida refrescante sabor cola de 600ml");
        product.setPurchasePrice(new BigDecimal("5.00"));
        product.setSalePrice(new BigDecimal("7.00"));
        product.setMinimumStock(10);
        product.setMaximumStock(80);
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setUnitOfMeasure(Product.UnitOfMeasure.UNIDAD);
        product.setStatus(Product.Status.ACTIVO);

        response = new ProductResponseDTO();
        response.setProductId(1L);
        response.setProductCode("PROD001");
        response.setProductName("Coca Cola");
        response.setDescription("Bebida refrescante sabor cola de 600ml");
        response.setPurchasePrice(new BigDecimal("5.00"));
        response.setSalePrice(new BigDecimal("7.00"));
        response.setMinimumStock(10);
        response.setMaximumStock(80);
        response.setCategoryId(1L);
        response.setSupplierId(1L);
        response.setUnitOfMeasure(Product.UnitOfMeasure.UNIDAD);
        response.setStatus(Product.Status.ACTIVO);

        productDTO = new ProductCreateDTO();
        productDTO.setProductCode("PROD001");
        productDTO.setProductName("Coca Cola");
        productDTO.setDescription("Bebida refrescante sabor cola de 600ml");
        productDTO.setPurchasePrice(new BigDecimal("5.00"));
        productDTO.setSalePrice(new BigDecimal("7.00"));
        productDTO.setMinimumStock(10);
        productDTO.setMaximumStock(80);
        productDTO.setCategoryId(1L);
        productDTO.setSupplierId(1L);
        productDTO.setUnitOfMeasure(Product.UnitOfMeasure.UNIDAD);

        update = new ProductUpdateDTO();
        update.setProductName("Fanta naranja");
        update.setDescription("Bebida fria sabor naranja de 600ml");
        update.setPurchasePrice(new BigDecimal("5.00"));
        update.setSalePrice(new BigDecimal("7.00"));
        update.setMinimumStock(12);
        update.setMaximumStock(65);
        update.setCategoryId(1L);
        update.setSupplierId(1L);
        update.setUnitOfMeasure(Product.UnitOfMeasure.UNIDAD);
    }

    @Test 
    void findProductById() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(mapper.toResponseDTO(product)).thenReturn(response);

        ProductResponseDTO result = service.findByIdProduct(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCategoryId());
        assertEquals("PROD001", result.getProductCode());
        assertEquals(Product.Status.ACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(mapper).toResponseDTO(product);
    }

    @Test 
    void findProductByIdReturnException() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findByIdProduct(10L));

        assertEquals("El producto 10 no existe", exception.getMessage());

        verifyNoInteractions(mapper);
    }

    @Test 
    void createProduct() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(mapper.toEntity(productDTO, category, supplier)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);
        when(mapper.toResponseDTO(product)).thenReturn(response);

        ProductResponseDTO result = service.createProduct(productDTO);

        assertNotNull(result);
        assertEquals("PROD001", result.getProductCode());
        assertEquals(1L, result.getProductId());
        assertEquals(Product.Status.ACTIVO, result.getStatus());

        verify(categoryRepository).findById(1L);
        verify(supplierRepository).findById(1L);
        verify(mapper).toEntity(productDTO, category, supplier);
        verify(repository).save(product);
        verify(mapper).toResponseDTO(product);
    }

    @Test 
    void createProductCategoryNotExist() {
        productDTO.setCategoryId(10L);

        when(categoryRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.createProduct(productDTO));

        assertEquals("La categoria 10 no existe", exception.getMessage());

        verifyNoInteractions(supplierRepository);
    }

    @Test 
    void createProductSupplierNotExist() {
        productDTO.setSupplierId(10L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(supplierRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.createProduct(productDTO));

        assertEquals("El proveedor 10 no existe", exception.getMessage());
    }

    @Test 
    void createProductInactiveCategory() {
        category.setStatus(Category.Status.INACTIVO);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.createProduct(productDTO));

        assertEquals("No se puede asociar el producto a una categoria inactiva", exception.getMessage());
        
        verifyNoInteractions(repository);
    }

    @Test 
    void createProductInactiveSupplier() {
        supplier.setStatus(Supplier.Status.INACTIVO);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.createProduct(productDTO));

        assertEquals("No se puede asociar el producto a un proveedor inactivo", exception.getMessage());
        
        verifyNoInteractions(repository);
    }

    @Test
    void createProductInvalidPrice() {
        productDTO.setPurchasePrice(new BigDecimal("10.00"));
        productDTO.setSalePrice(new BigDecimal("5.00"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.createProduct(productDTO));

        assertEquals("El precio de venta no puede ser menor que el precio de compra", exception.getMessage());
    }

    @Test
    void createProductInvalidStock() {
        productDTO.setMinimumStock(40);
        productDTO.setMaximumStock(30);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.createProduct(productDTO));

        assertEquals("El stock máximo debe ser mayor que el stock mínimo", exception.getMessage());
    }
    
    @Test
    void updateProduct() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(repository.save(product)).thenReturn(product);
        when(mapper.toResponseDTO(product)).thenReturn(response);

        ProductResponseDTO result = service.updateProduct(1L, update);

        assertNotNull(result);

        verify(mapper).updateEntity(update, product, category, supplier);
        verify(repository).save(product);
    }

    @Test
    void updateProductCategoryNotExist() {
        update.setCategoryId(10L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updateProduct(1L, update));

        assertEquals("La categoria 10 no existe", exception.getMessage());
    }

    @Test
    void updateProductSupplierNotFound() {
        update.setSupplierId(10L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(supplierRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updateProduct(1L, update));

        assertEquals("El proveedor 10 no existe", exception.getMessage());
    }

    @Test
    void updateProductInvalidPrice() {
        update.setPurchasePrice(new BigDecimal("10.00"));
        update.setSalePrice(new BigDecimal("5.00"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.updateProduct(1L, update));

        assertEquals("El precio de venta no puede ser menor que el precio de compra", exception.getMessage());
    }

    @Test
    void updateProductInvalidStock() {
        update.setMinimumStock(50);
        update.setMaximumStock(30);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.updateProduct(1L, update));

        assertEquals("El stock máximo debe ser mayor que el stock mínimo", exception.getMessage());
    }

    @Test
    void changeStatusInactive() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
        when(mapper.toResponseDTO(product)).thenReturn(response);

        ProductResponseDTO result = service.changeStatus(1L, Product.Status.INACTIVO);

        assertNotNull(result);
        assertEquals(Product.Status.INACTIVO, product.getStatus());

        verify(repository).save(product);
        verify(mapper).toResponseDTO(product);
    }

    @Test
    void changeStatusActive() {
        product.setStatus(Product.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
        when(mapper.toResponseDTO(product)).thenReturn(response);

        ProductResponseDTO result = service.changeStatus(1L, Product.Status.ACTIVO);

        assertNotNull(result);
        assertEquals(Product.Status.ACTIVO, product.getStatus());

        verify(repository).save(product);
        verify(mapper).toResponseDTO(product);
    }

    @Test
    void changeStatusAlreadyActive() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Product.Status.ACTIVO));

        assertEquals("El producto ya esta ACTIVO", exception.getMessage());
    }

    @Test
    void changeStatusAlreadyInactive() {
        product.setStatus(Product.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Product.Status.INACTIVO));

        assertEquals("El producto ya esta INACTIVO", exception.getMessage());
    }

}

