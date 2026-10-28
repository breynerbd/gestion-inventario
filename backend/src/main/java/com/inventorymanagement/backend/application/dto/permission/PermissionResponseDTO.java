package com.inventorymanagement.backend.application.dto.permission;

import java.time.LocalDateTime;

import com.inventorymanagement.backend.domain.entity.Permission.Module;
import com.inventorymanagement.backend.domain.entity.Permission.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PermissionResponseDTO {

    private Long permissionId;
    private String permissionCode;
    private String permissionName;
    private Module module;
    private String description;
    private Status status;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
}