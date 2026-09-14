package com.inventorymanagement.backend.application.dto.user;

import com.inventorymanagement.backend.domain.entity.User.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class UserResponseDTO {

    private Long userId;
    private String username;
    private String firstNames;
    private String lastNames;
    private String email;
    private String phone;
    private Long roleId;
    private String roleName;
    private Status status;
    private Integer failedAttempts;
}