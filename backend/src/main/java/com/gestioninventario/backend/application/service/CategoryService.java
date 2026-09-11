package com.gestioninventario.backend.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestioninventario.backend.domain.entity.Category;
import com.gestioninventario.backend.domain.exception.ResourceNotFoundException;
import com.gestioninventario.backend.domain.exception.StatusUnchangedException;
import com.gestioninventario.backend.application.dto.category.CategoryCreateDTO;
import com.gestioninventario.backend.application.dto.category.CategoryResponseDTO;
import com.gestioninventario.backend.application.dto.category.CategoryUpdateDTO;
import com.gestioninventario.backend.application.mapper.CategoryMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private static final String CATEGORY_NOT_FOUND = "La categoria ";
    private static final String CATEGORY_NOT_EXIST = " no existe";

    public Page<CategoryResponseDTO> findAllCategories(Pageable pageable){
        return repository.findAll(pageable).map(mapper::toResponseDTO);
    }

    public CategoryResponseDTO findCategoryById(Long categoryId){
        Category category = repository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND + categoryId + CATEGORY_NOT_EXIST));

        return mapper.toResponseDTO(category);
    }

    public CategoryResponseDTO createCategory(CategoryCreateDTO categoryDTO){
        Category category = mapper.toEntity(categoryDTO);

        Category savedCategory = repository.save(category); 
        return mapper.toResponseDTO(savedCategory);
    }

    public CategoryResponseDTO updateCategory(Long categoryId, CategoryUpdateDTO categoryDTO){
        Category category = repository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND + categoryId + CATEGORY_NOT_EXIST));

        mapper.updateEntity(categoryDTO, category);

        Category updatedCategory = repository.save(category);

        return mapper.toResponseDTO(updatedCategory);
    }

    public CategoryResponseDTO changeStatus(Long categoryId,Category.Status estado) {

    Category category = repository.findById(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND + categoryId + CATEGORY_NOT_EXIST));

    if (category.getStatus() == estado) {
        String message = switch (estado) {
            case ACTIVO -> "La categoria ya esta activa";
            case INACTIVO -> "La categoria ya esta inactiva";
        };

        throw new StatusUnchangedException(message);
    }

    category.setStatus(estado);

    Category categoryActualizada = repository.save(category);

    return mapper.toResponseDTO(categoryActualizada);
}
}