package com.inventorymanagement.backend.application.dto.category;

import com.inventorymanagement.backend.domain.entity.Category.Status;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryStatusDTO {

    @NotNull(message = "El estado es obligatorio")
    private Status status;
}