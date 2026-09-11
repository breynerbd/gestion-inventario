package com.gestioninventario.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.application.dto.product.ProductCreateDTO;
import com.gestioninventario.backend.application.dto.product.ProductResponseDTO;
import com.gestioninventario.backend.application.dto.product.ProductUpdateDTO;
import com.gestioninventario.backend.domain.entity.Category;
import com.gestioninventario.backend.domain.entity.Product;
import com.gestioninventario.backend.domain.entity.Supplier;

@Component
public class ProductMapper {

    public Product toEntity(ProductCreateDTO dto, Category category, Supplier supplier) {

        Product product = new Product();

        product.setProductCode(dto.getProductCode());
        product.setProductName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setUnitOfMeasure(dto.getUnitOfMeasure());
        product.setPurchasePrice(dto.getPurchasePrice());
        product.setSalePrice(dto.getSalePrice());
        product.setCurrentStock(0);
        product.setMinimumStock(dto.getMinimumStock());
        product.setMaximumStock(dto.getMaximumStock());
        product.setStatus(Product.Status.ACTIVO);

        return product;
    }

    public void updateEntity(ProductUpdateDTO dto, Product product, Category category, Supplier supplier) {

        product.setProductName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setUnitOfMeasure(dto.getUnitOfMeasure());
        product.setPurchasePrice(dto.getPurchasePrice());
        product.setSalePrice(dto.getSalePrice());
        product.setMinimumStock(dto.getMinimumStock());
        product.setMaximumStock(dto.getMaximumStock());
    }

    public ProductResponseDTO toResponseDTO(Product product) {

        ProductResponseDTO dto = new ProductResponseDTO();

        dto.setProductId(product.getProductId());
        dto.setProductCode(product.getProductCode());
        dto.setProductName(product.getProductName());
        dto.setDescription(product.getDescription());
        dto.setCategoryId(product.getCategory().getCategoryId());
        dto.setSupplierId(product.getSupplier().getSupplierId());
        dto.setUnitOfMeasure(product.getUnitOfMeasure());
        dto.setPurchasePrice(product.getPurchasePrice());
        dto.setSalePrice(product.getSalePrice());
        dto.setCurrentStock(product.getCurrentStock());
        dto.setMinimumStock(product.getMinimumStock());
        dto.setMaximumStock(product.getMaximumStock());
        dto.setStatus(product.getStatus());

        return dto;
    }
}