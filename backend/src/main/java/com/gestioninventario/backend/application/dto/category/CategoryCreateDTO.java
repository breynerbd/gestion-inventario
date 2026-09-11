package com.gestioninventario.backend.application.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter 
@Setter 
@NoArgsConstructor 
public class CategoryCreateDTO {
    
    @NotBlank(message = "El código de la categoría es obligatorio")
    @Size(max = 10, message = "El código de la categoría no puede superar los 10 caracteres")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "El código de la categoría solo puede tener letras mayúsculas, números, guiones y guiones bajos (Ejemplo: CAT001)")
    private String categoryCode;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 60, message = "El nombre de la categoría no puede superar los 60 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚÑáéíóúñ\\s]+$", message = "El nombre de la categoría solo puede tener letras y espacios")
    private String categoryName;

    @Size(max = 250, message = "La descripción no puede superar los 250 caracteres")
    private String description;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}