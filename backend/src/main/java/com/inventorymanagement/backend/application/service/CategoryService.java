package com.inventorymanagement.backend.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.inventorymanagement.backend.domain.entity.Category;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.application.dto.category.CategoryCreateDTO;
import com.inventorymanagement.backend.application.dto.category.CategoryResponseDTO;
import com.inventorymanagement.backend.application.dto.category.CategoryUpdateDTO;
import com.inventorymanagement.backend.application.mapper.CategoryMapper;
import com.inventorymanagement.backend.infrastructure.persistence.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    private static final String CATEGORY_NOT_FOUND = "La categoria ";
    private static final String CATEGORY_NOT_EXIST = " no existe";
    private static final String LOGGER_NOT_FOUND = "No se encontro la categoria: {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

    public Page<CategoryResponseDTO> findAllCategories(Pageable pageable){
        LOGGER.debug("Obteniendo datos de categorias existentes");
        return repository.findAll(pageable).map(mapper::toResponseDTO);
    }

    public CategoryResponseDTO findCategoryById(Long categoryId){
        LOGGER.debug("Buscando categoria: {}", categoryId);

        Category category = repository.findById(categoryId)
            .orElseThrow(() -> { 
                LOGGER.warn(LOGGER_NOT_FOUND, categoryId);
                return new ResourceNotFoundException(CATEGORY_NOT_FOUND + categoryId + CATEGORY_NOT_EXIST);}
            );

        return mapper.toResponseDTO(category);
    }

    public CategoryResponseDTO createCategory(CategoryCreateDTO categoryDTO){
        LOGGER.debug("Creando una categoria");

        Category category = mapper.toEntity(categoryDTO);

        Category savedCategory = repository.save(category); 
        LOGGER.info("Se creo la categoria con id: {}", savedCategory.getCategoryId());

        return mapper.toResponseDTO(savedCategory);
    }

    public CategoryResponseDTO updateCategory(Long categoryId, CategoryUpdateDTO categoryDTO){
        LOGGER.debug("Actualizando categoria: {}", categoryId);
        
        Category category = repository.findById(categoryId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, categoryId);
                return new ResourceNotFoundException(CATEGORY_NOT_FOUND + categoryId + CATEGORY_NOT_EXIST);}
            );

        mapper.updateEntity(categoryDTO, category);

        Category updatedCategory = repository.save(category);

        LOGGER.info("La categoria {} se ha actualizado", categoryId);

        return mapper.toResponseDTO(updatedCategory);
    }

    public CategoryResponseDTO changeStatus(Long categoryId,Category.Status status) {
        LOGGER.debug("Cambiando estado de la categoria {} a {}", categoryId, status);

        Category category = repository.findById(categoryId)
            .orElseThrow(() -> {
                LOGGER.warn(LOGGER_NOT_FOUND, categoryId);
                return new ResourceNotFoundException(CATEGORY_NOT_FOUND + categoryId + CATEGORY_NOT_EXIST);}
            );

        if (category.getStatus() == status) {
            String message = switch (status) {
                case ACTIVO -> "La categoria ya esta activa";
                case INACTIVO -> "La categoria ya esta inactiva";
            };

            throw new StatusUnchangedException(message);
        }

        category.setStatus(status);

        Category categoryActualizada = repository.save(category);

        LOGGER.info("El estado de la categoria {} se ha cambiado a {}", categoryId, status);

        return mapper.toResponseDTO(categoryActualizada);
    }
}