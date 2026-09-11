package com.gestioninventario.backend.application.dto.permission;

import com.gestioninventario.backend.domain.entity.Permission.Module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PermissionCreateDTO {

    @NotBlank(message = "El código del permiso es obligatorio")
    @Size(max = 60, message = "El código del permiso no puede superar los 60 caracteres")
    @Pattern(
        regexp = "^[A-Z0-9_]+$",
        message = "El código del permiso solo puede tener letras mayúsculas, números y guiones bajos (USERS_CREATE)"
    )
    private String permissionCode;

    @NotBlank(message = "El nombre del permiso es obligatorio")
    @Size(max = 100, message = "El nombre del permiso no puede superar los 100 caracteres")
    private String permissionName;

    @NotNull(message = "El módulo es obligatorio")
    private Module module;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String description;
}