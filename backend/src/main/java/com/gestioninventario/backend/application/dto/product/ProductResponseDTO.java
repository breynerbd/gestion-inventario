package com.gestioninventario.backend.application.dto.product;

import java.math.BigDecimal;

import com.gestioninventario.backend.domain.entity.Product.Status;
import com.gestioninventario.backend.domain.entity.Product.UnitOfMeasure;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class ProductResponseDTO {

    private Long productId;
    private String productCode;
    private String productName;
    private String description;
    private Long categoryId;
    private Long supplierId;
    private UnitOfMeasure unitOfMeasure;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private Integer currentStock;
    private Integer minimumStock;
    private Integer maximumStock;
    private Status status;
}