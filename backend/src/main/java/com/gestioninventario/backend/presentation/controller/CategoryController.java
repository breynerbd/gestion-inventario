package com.gestioninventario.backend.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestioninventario.backend.application.dto.category.CategoryCreateDTO;
import com.gestioninventario.backend.application.dto.category.CategoryStatusDTO;
import com.gestioninventario.backend.application.dto.category.CategoryResponseDTO;
import com.gestioninventario.backend.application.dto.category.CategoryUpdateDTO;
import com.gestioninventario.backend.application.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDTO>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findAllCategories(pageable));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(service.findCategoryById(categoryId));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryCreateDTO categoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCategory(categoryDto));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long categoryId, @Valid @RequestBody CategoryUpdateDTO categoryDto){
        return ResponseEntity.ok(service.updateCategory(categoryId, categoryDto));
    }

    @PatchMapping("/{categoryId}/status")
    public ResponseEntity<CategoryResponseDTO> changeStatus(@PathVariable Long categoryId, @Valid @RequestBody CategoryStatusDTO statusDto) {
        return ResponseEntity.ok(service.changeStatus(categoryId, statusDto.getStatus()));
    }
}