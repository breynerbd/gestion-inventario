package com.gestioninventario.backend.application.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class RoleUpdateDTO {

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50, message = "El nombre del rol no puede superar los 50 caracteres")
    @Pattern(regexp = "^[A-Z ]+$", message = "El nombre del rol solo puede contener letras mayúsculas y espacios")
    private String roleName;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String description;
}