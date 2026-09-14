package com.inventorymanagement.backend.application.dto.role;

import com.inventorymanagement.backend.domain.entity.Role.Status;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class RoleStatusDTO {

    @NotNull(message = "El estado es obligatorio")
    private Status status;
}