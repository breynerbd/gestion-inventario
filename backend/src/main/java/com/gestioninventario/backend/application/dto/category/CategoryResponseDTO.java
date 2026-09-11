package com.gestioninventario.backend.application.dto.category;

import java.time.LocalDateTime;

import com.gestioninventario.backend.domain.entity.Category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryResponseDTO {

    private Long categoryId;
    private String categoryCode;
    private String categoryName;
    private String description;
    private Category.Status status;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
}