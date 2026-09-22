package com.inventorymanagement.backend.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventorymanagement.backend.application.dto.category.CategoryCreateDTO;
import com.inventorymanagement.backend.application.dto.category.CategoryStatusDTO;
import com.inventorymanagement.backend.application.dto.category.CategoryResponseDTO;
import com.inventorymanagement.backend.application.dto.category.CategoryUpdateDTO;
import com.inventorymanagement.backend.application.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDTO>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        LOGGER.info("Solicitud para obtener todas las categorias");
        return ResponseEntity.ok(service.findAllCategories(pageable));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long categoryId) {
        LOGGER.info("Solicitud para obtener la categoria con id {}", categoryId);
        return ResponseEntity.ok(service.findCategoryById(categoryId));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryCreateDTO categoryDto) {
        LOGGER.info("Solicitud para crear una categoria");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCategory(categoryDto));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long categoryId, @Valid @RequestBody CategoryUpdateDTO categoryDto){
        LOGGER.info("Solicitud para actualizar la categoria con id {}", categoryId);
        return ResponseEntity.ok(service.updateCategory(categoryId, categoryDto));
    }

    @PatchMapping("/{categoryId}/status")
    public ResponseEntity<CategoryResponseDTO> changeStatus(@PathVariable Long categoryId, @Valid @RequestBody CategoryStatusDTO statusDto) {
        LOGGER.info("Solicitud para cambiar el estado de la categoria con id {}", categoryId);
        return ResponseEntity.ok(service.changeStatus(categoryId, statusDto.getStatus()));
    }
}