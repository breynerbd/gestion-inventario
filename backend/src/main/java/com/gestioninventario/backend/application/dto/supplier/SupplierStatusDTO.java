package com.gestioninventario.backend.application.dto.supplier;

import com.gestioninventario.backend.domain.entity.Supplier.Status;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class SupplierStatusDTO {

    @NotNull(message = "El estado es obligatorio")
    private Status status;
}