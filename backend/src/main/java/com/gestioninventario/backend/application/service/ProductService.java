package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.domain.entity.Category;
import com.gestioninventario.backend.domain.entity.Product;
import com.gestioninventario.backend.domain.entity.Supplier;
import com.gestioninventario.backend.domain.exception.StatusUnchangedException;
import com.gestioninventario.backend.domain.exception.ResourceNotFoundException;
import com.gestioninventario.backend.application.dto.product.ProductCreateDTO;
import com.gestioninventario.backend.application.dto.product.ProductResponseDTO;
import com.gestioninventario.backend.application.dto.product.ProductUpdateDTO;
import com.gestioninventario.backend.application.mapper.ProductMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.CategoryRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.ProductRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

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

    private void validateActiveEntities(Category category, Supplier proveedor) {
        if (category.getStatus() == Category.Status.INACTIVO) {
            throw new IllegalArgumentException("No se puede asociar el producto a una category inactiva");
        }

        if (proveedor.getStatus() == Supplier.Status.INACTIVO) {
            throw new IllegalArgumentException("No se puede asociar el producto a un proveedor INACTIVO");
        }
    }

    private void validateProductRules(BigDecimal purchasePrice, BigDecimal salePrice, Integer minimumStock, Integer maximumStock) {
        if (salePrice.compareTo(purchasePrice) < 0) {
            throw new IllegalArgumentException("El precio de venta no puede ser menor que el precio de compra");
        }

        if (maximumStock != null && maximumStock <= minimumStock) {
            throw new IllegalArgumentException("El stock máximo debe ser mayor que el stock mínimo");
        }
    }

    public Page<ProductResponseDTO> findAllProducts(String productCode, String productName, Long categoryId, Long supplierId, Product.Status status, Pageable pageable) {
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
        Product product = repository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND + productId + NOT_EXIST));

        return mapper.toResponseDTO(product);
    }

    public ProductResponseDTO createProduct(ProductCreateDTO productDto) {
        validateProductRules(
            productDto.getPurchasePrice(),
            productDto.getSalePrice(),
            productDto.getMinimumStock(),
            productDto.getMaximumStock()
        );

        Category category = categoryRepository.findById(productDto.getCategoryId()) 
            .orElseThrow(() -> new ResourceNotFoundException( "La category " + productDto.getCategoryId() + NOT_EXIST ));

        Supplier supplier = proveedorRepository.findById(productDto.getSupplierId()) 
            .orElseThrow(() -> new ResourceNotFoundException( "El proveedor " + productDto.getSupplierId() + NOT_EXIST));
        
        validateActiveEntities(category, supplier);

        Product product = mapper.toEntity(productDto, category, supplier);
         
        Product savedProduct = repository.save(product); 
         
        return mapper.toResponseDTO(savedProduct);
    }

    public ProductResponseDTO updateProduct(Long productId, ProductUpdateDTO productDto) {
        validateProductRules(
            productDto.getPurchasePrice(),
            productDto.getSalePrice(),
            productDto.getMinimumStock(),
            productDto.getMaximumStock()
        );

        Product product = repository.findById(productId) 
            .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND + productId + NOT_EXIST)); 
            
        Category category = categoryRepository.findById(productDto.getCategoryId()) 
            .orElseThrow(() -> new ResourceNotFoundException("La category " + productDto.getCategoryId() + NOT_EXIST)); 
            
        Supplier supplier = proveedorRepository.findById(productDto.getSupplierId()) 
            .orElseThrow(() -> new ResourceNotFoundException("El proveedor " + productDto.getSupplierId() + NOT_EXIST));
        
        validateActiveEntities(category, supplier);

        mapper.updateEntity(productDto, product, category, supplier);
        
        Product updatedProduct = repository.save(product); 
        
        return mapper.toResponseDTO(updatedProduct);
    }

    public ProductResponseDTO changeStatus(Long productId, Product.Status status) {

    Product product = repository.findById(productId)
        .orElseThrow(() ->new ResourceNotFoundException(PRODUCT_NOT_FOUND + productId + NOT_EXIST));

    if (product.getStatus() == status) {
        String message = switch (status) {
            case ACTIVO -> "El producto ya esta ACTIVO";
            case INACTIVO -> "El producto ya esta INACTIVO";
        };

        throw new StatusUnchangedException(message);
    }

    product.setStatus(status);

    Product updatedProduct = repository.save(product);

    return mapper.toResponseDTO(updatedProduct);
}
}