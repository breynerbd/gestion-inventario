package com.gestioninventario.backend.application.dto.product;

import com.gestioninventario.backend.domain.entity.Product.Status;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class ProductStatusDTO {

    @NotNull(message = "El estado es obligatorio")
    private Status status;
}