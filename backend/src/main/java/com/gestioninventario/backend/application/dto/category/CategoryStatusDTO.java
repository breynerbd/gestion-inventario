package com.gestioninventario.backend.application.dto.category;

import com.gestioninventario.backend.domain.entity.Category.Status;

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