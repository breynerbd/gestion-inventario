package com.gestioninventario.backend.application.dto.permission;

import com.gestioninventario.backend.domain.entity.Permission.Status;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PermissionStatusDTO {

    @NotNull(message = "El estado es obligatorio")
    private Status status;
}