package com.inventorymanagement.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.inventorymanagement.backend.application.dto.category.CategoryCreateDTO;
import com.inventorymanagement.backend.application.dto.category.CategoryResponseDTO;
import com.inventorymanagement.backend.application.dto.category.CategoryUpdateDTO;
import com.inventorymanagement.backend.domain.entity.Category;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryCreateDTO dto) {

        Category category = new Category();

        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        category.setStatus(Category.Status.ACTIVO);

        return category;
    }

    public void updateEntity(CategoryUpdateDTO dto, Category category) {

        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
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