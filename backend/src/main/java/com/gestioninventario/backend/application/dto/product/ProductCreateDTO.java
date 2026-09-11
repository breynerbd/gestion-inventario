package com.gestioninventario.backend.application.dto.product;

import java.math.BigDecimal;

import com.gestioninventario.backend.domain.entity.Product.UnitOfMeasure;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class ProductCreateDTO {

    @NotBlank(message = "El código del producto es obligatorio")
    @Size(max = 20, message = "El código del producto no puede superar los 20 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "El código del producto solo puede tener letras, números, guiones y guiones bajos (Ejemplo: PROD001)")
    private String productCode;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 120, message = "El nombre del producto no puede superar los 120 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚÑáéíóúñ0-9\\s]+$", message = "El nombre del producto solo puede tener letras, números y espacios")
    private String productName;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String description;

    @NotNull(message = "La categoría es obligatoria")
    @Positive(message = "El identificador de la categoría debe ser mayor que 0")
    private Long categoryId;

    @NotNull(message = "El proveedor es obligatorio")
    @Positive(message = "El identificador del proveedor debe ser mayor que 0")
    private Long supplierId;

    @NotNull(message = "La unidad de medida es obligatoria")
    private UnitOfMeasure unitOfMeasure;

    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de compra debe ser mayor que 0")
    private BigDecimal purchasePrice;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor que 0")
    private BigDecimal salePrice;

    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer minimumStock;

    @Min(value = 0, message = "El stock máximo no puede ser negativo")
    private Integer maximumStock;
}