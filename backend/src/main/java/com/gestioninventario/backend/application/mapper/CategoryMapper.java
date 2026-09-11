package com.gestioninventario.backend.application.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.application.dto.category.CategoryCreateDTO;
import com.gestioninventario.backend.application.dto.category.CategoryResponseDTO;
import com.gestioninventario.backend.application.dto.category.CategoryUpdateDTO;
import com.gestioninventario.backend.domain.entity.Category;

@Component
public class CategoryMapper {
    private static final ZoneId ZONE_ID = ZoneId.of("America/Guatemala");

    public Category toEntity(CategoryCreateDTO dto) {

        Category category = new Category();

        category.setCategoryCode(dto.getCategoryCode());
        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        category.setStatus(Category.Status.ACTIVO); 
        LocalDateTime now = LocalDateTime.now(ZONE_ID); 
        category.setCreationDate(now); 
        category.setModificationDate(now); 

        return category;
    }

    public void updateEntity(CategoryUpdateDTO dto, Category category) {

        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        category.setModificationDate(LocalDateTime.now(ZONE_ID));
    }

    public CategoryResponseDTO toResponseDTO(Category category) {

        CategoryResponseDTO dto = new CategoryResponseDTO();

        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryCode(category.getCategoryCode());
        dto.setCategoryName(category.getCategoryName());
        dto.setDescription(category.getDescription());
        dto.setStatus(category.getStatus());
        dto.setCreationDate(category.getCreationDate());
        dto.setModificationDate(category.getModificationDate());

        return dto;
    }
}