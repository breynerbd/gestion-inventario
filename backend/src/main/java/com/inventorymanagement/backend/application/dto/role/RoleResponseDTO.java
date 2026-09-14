package com.inventorymanagement.backend.application.dto.role;

import com.inventorymanagement.backend.domain.entity.Role.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class RoleResponseDTO {

    private Long roleId;
    private String roleName;
    private String description;
    private Status status;
}