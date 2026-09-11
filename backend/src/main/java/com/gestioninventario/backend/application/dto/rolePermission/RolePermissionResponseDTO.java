package com.gestioninventario.backend.application.dto.rolePermission;

import java.time.LocalDateTime;

import com.gestioninventario.backend.domain.entity.Permission.Module;
import com.gestioninventario.backend.domain.entity.Permission.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RolePermissionResponseDTO {

    private Long permissionId;
    private String permissionCode;
    private String permissionName;
    private Module module;
    private String description;
    private Status status;
    private LocalDateTime assignmentDate;
}