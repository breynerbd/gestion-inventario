package com.gestioninventario.backend.application.dto.user;

import com.gestioninventario.backend.domain.entity.User.Status;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class UserStatusDTO {
    
    @NotNull(message = "El estado es obligatorio")
    private Status status;
}