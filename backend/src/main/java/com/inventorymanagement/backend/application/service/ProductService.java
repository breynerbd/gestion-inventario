package com.inventorymanagement.backend.application.service;

import com.inventorymanagement.backend.domain.entity.Category;
import com.inventorymanagement.backend.domain.entity.Product;
import com.inventorymanagement.backend.domain.entity.Supplier;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.application.dto.product.ProductCreateDTO;
import com.inventorymanagement.backend.application.dto.product.ProductResponseDTO;
import com.inventorymanagement.backend.application.dto.product.ProductUpdateDTO;
import com.inventorymanagement.backend.application.mapper.ProductMapper;
import com.inventorymanagement.backend.infrastructure.persistence.repository.CategoryRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.ProductRepository;
import com.inventorymanagement.backend.infrastructure.persistence.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository proveedorRepository;
    private final ProductMapper mapper;
    private static final String PRODUCT_NOT_FOUND = "El producto ";
    private static final String NOT_EXIST = " no existe";
    private static final String LOGGER_NOT_FOUND = "No se encontro el producto: {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private void validateActiveEntities(Category category, Supplier proveedor) {
        LOGGER.debug("Validando categoria y proveedor activos");

        if (category.getStatus() == Category.Status.INACTIVO) {
            LOGGER.warn("La categoria {} esta inactiva", category.getCategoryId());
            throw new IllegalArgumentException("No se puede asociar el producto a una categoria inactiva");
        }

        if (proveedor.getStatus() == Supplier.Status.INACTIVO) {
            LOGGER.warn("El proveedor {} esta inactivo", proveedor.getSupplierId());
            throw new IllegalArgumentException("No se puede asociar el producto a un proveedor inactivo");
        }
    }

    private void validateProductRules(BigDecimal purchasePrice, BigDecimal salePrice, Integer minimumStock, Integer maximumStock) {
        LOGGER.debug("Validando precio de venta y stocks");

        if (salePrice.compareTo(purchasePrice) < 0) {
            LOGGER.warn("El precio de venta es menor que el precio de compra");
            throw new IllegalArgumentException("El precio de venta no puede ser menor que el precio de compra");
        }

        if (maximumStock != null && maximumStock <= minimumStock) {
            LOGGER.warn("El stock maximo no es mayor que el stock minimo");
            throw new IllegalArgumentException("El stock máximo debe ser mayor que el stock mínimo");
        }
    }

    public Page<ProductResponseDTO> findAllProducts(String productCode, String productName, Long categoryId, Long supplierId, Product.Status status, Pageable pageable) {
        LOGGER.debug("Obteniendo datos de productos existentes");

        Specification<Product> specification = Specification.unrestricted();
        if(productCode != null && !productCode.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) 
                -> criteriaBuilder.like(criteriaBuilder.lower(root.get("productCode")), "%" + productCode.toLowerCase() + "%"));
        }

        if(productName != null && !productName.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) 
                -> criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + productName.toLowerCase() + "%"));
        }

        if (categoryId != null) {
            specification = specification.and((root, query, criteriaBuilder) 
                -> criteriaBuilder.equal(root.get("category").get("categoryId"),categoryId));
        }

        if (supplierId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("supplier").get("supplierId"),supplierId));
        }

        if (status != null) {
            specification = specification.and(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
        }

        return repository.findAll(specification, pageable).map(mapper::toResponseDTO);
    }

    public ProductResponseDTO findByIdProduct(Long productId) {
        LOGGER.debug("Buscando producto: {}", productId);
        Product product = repository.findById(productId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND);
                return new ResourceNotFoundException(PRODUCT_NOT_FOUND + productId + NOT_EXIST);
            });
        
        return mapper.toResponseDTO(product);
    }

    public ProductResponseDTO createProduct(ProductCreateDTO productDto) {
        validateProductRules(
            productDto.getPurchasePrice(),
            productDto.getSalePrice(),
            productDto.getMinimumStock(),
            productDto.getMaximumStock()
        );

        LOGGER.debug("Creando Producto");

        Category category = categoryRepository.findById(productDto.getCategoryId()) 
            .orElseThrow(() -> {
                LOGGER.warn("No se encontro la categoria: {}", productDto.getCategoryId());
                return new ResourceNotFoundException( "La categoria " + productDto.getCategoryId() + NOT_EXIST );
            });

        Supplier supplier = proveedorRepository.findById(productDto.getSupplierId()) 
            .orElseThrow(() -> {
                LOGGER.warn("No se encontro el proveedor: {}", productDto.getSupplierId());
                return new ResourceNotFoundException( "El proveedor " + productDto.getSupplierId() + NOT_EXIST);}
            );
        
        validateActiveEntities(category, supplier);

        Product product = mapper.toEntity(productDto, category, supplier);
         
        Product savedProduct = repository.save(product); 

        LOGGER.info("Se creo el producto: {}", savedProduct.getProductId());
         
        return mapper.toResponseDTO(savedProduct);
    }

    public ProductResponseDTO updateProduct(Long productId, ProductUpdateDTO productDto) {
        validateProductRules(
            productDto.getPurchasePrice(),
            productDto.getSalePrice(),
            productDto.getMinimumStock(),
            productDto.getMaximumStock()
        );

        LOGGER.debug("Actualizando producto: {}", productId);

        Product product = repository.findById(productId) 
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, productId);
                return new ResourceNotFoundException(PRODUCT_NOT_FOUND + productId + NOT_EXIST);}
            );

        Category category = categoryRepository.findById(productDto.getCategoryId()) 
            .orElseThrow(() -> {
                LOGGER.warn("No se encontro la categoria: {}", productDto.getCategoryId());
                return new ResourceNotFoundException("La categoria " + productDto.getCategoryId() + NOT_EXIST);
            });

        Supplier supplier = proveedorRepository.findById(productDto.getSupplierId()) 
            .orElseThrow(() -> {
                LOGGER.warn("No se encontro el proveedor: {}", productDto.getSupplierId());
                return new ResourceNotFoundException("El proveedor " + productDto.getSupplierId() + NOT_EXIST);
            });
        
        validateActiveEntities(category, supplier);

        mapper.updateEntity(productDto, product, category, supplier);
        
        Product updatedProduct = repository.save(product); 

        LOGGER.info("El producto {} se ha actualizado", productId);
        
        return mapper.toResponseDTO(updatedProduct);
    }

    public ProductResponseDTO changeStatus(Long productId, Product.Status status) {
        LOGGER.debug("Cambiando estado del producto {} a {}", productId, status);

        Product product = repository.findById(productId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, productId);
                return new ResourceNotFoundException(PRODUCT_NOT_FOUND + productId + NOT_EXIST);}
            );

        if (product.getStatus() == status) {
            String message = switch (status) {
                case ACTIVO -> "El producto ya esta ACTIVO";
                case INACTIVO -> "El producto ya esta INACTIVO";
            };

            throw new StatusUnchangedException(message);
        }

        product.setStatus(status);

        Product updatedProduct = repository.save(product);

        LOGGER.info("El estado del producto {} se ha cambiado a {}", productId, status);

        return mapper.toResponseDTO(updatedProduct);
    }
}