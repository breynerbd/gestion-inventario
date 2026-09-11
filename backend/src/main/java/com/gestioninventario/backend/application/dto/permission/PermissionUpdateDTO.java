package com.gestioninventario.backend.application.dto.permission;

import com.gestioninventario.backend.domain.entity.Permission.Module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PermissionUpdateDTO {

    @NotBlank(message = "El nombre del permiso es obligatorio")
    @Size(max = 100, message = "El nombre del permiso no puede superar los 100 caracteres")
    private String permissionName;

    @NotNull(message = "El módulo es obligatorio")
    private Module module;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String description;
}