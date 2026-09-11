package com.gestioninventario.backend.application.dto.rolePermission;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RolePermissionDTO {

    @NotNull(message = "La lista de permisos es obligatoria")
    private List<Long> permissionIds;
}