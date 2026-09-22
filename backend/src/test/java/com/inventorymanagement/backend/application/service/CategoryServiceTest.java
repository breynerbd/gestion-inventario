package com.inventorymanagement.backend.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.inventorymanagement.backend.application.dto.category.CategoryCreateDTO;
import com.inventorymanagement.backend.application.dto.category.CategoryResponseDTO;
import com.inventorymanagement.backend.application.dto.category.CategoryUpdateDTO;
import com.inventorymanagement.backend.application.mapper.CategoryMapper;
import com.inventorymanagement.backend.domain.entity.Category;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.infrastructure.persistence.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock 
    private CategoryRepository repository;

    @Mock 
    private CategoryMapper mapper;

    @InjectMocks 
    private CategoryService service;

    private Category category;
    private CategoryResponseDTO response;
    private CategoryCreateDTO categoryDTO;
    private CategoryUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(1L);
        category.setCategoryCode("CAT001");
        category.setCategoryName("Embutidos");
        category.setDescription("Carnes procesadas frescas");
        category.setStatus(Category.Status.ACTIVO);
        category.setCreationDate(LocalDateTime.now());
        category.setModificationDate(LocalDateTime.now());

        response = new CategoryResponseDTO();
        response.setCategoryId(1L);
        response.setCategoryCode("CAT001");
        response.setCategoryName("Embutidos");
        response.setDescription("Carnes procesadas frescas");
        response.setStatus(Category.Status.ACTIVO);

        categoryDTO = new CategoryCreateDTO();
        categoryDTO.setCategoryCode("CAT002");
        categoryDTO.setCategoryName("Bebidas");
        categoryDTO.setDescription("Productos de bebidas");

        updateDTO = new CategoryUpdateDTO();
        updateDTO.setCategoryName("Bebidas Frias");
        updateDTO.setDescription("Productos de bebidas frias");
    }

    @Test 
    void findCategoryById() {
        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.toResponseDTO(category)).thenReturn(response);

        CategoryResponseDTO result = service.findCategoryById(1L);

        assertNotNull(result);
        assertEquals("CAT001", result.getCategoryCode());
        assertEquals("Embutidos", result.getCategoryName());
        assertEquals(Category.Status.ACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(mapper).toResponseDTO(category);
    }

    @Test
    void findCategoryByIdReturnException() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findCategoryById(10L));
        assertEquals("La categoria 10 no existe", exception.getMessage());

        verify(repository).findById(10L);
        verifyNoInteractions(mapper);
    }

    @Test 
    void findAllCategories() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Category> categoryPage = new PageImpl<>(List.of(category));

        when(repository.findAll(pageable)).thenReturn(categoryPage);
        when(mapper.toResponseDTO(category)).thenReturn(response);

        Page<CategoryResponseDTO> result = service.findAllCategories(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("CAT001", result.getContent().get(0).getCategoryCode());

        verify(repository).findAll(pageable);
        verify(mapper).toResponseDTO(category);
    }

    @Test 
    void createCategory() {
        Category saved = new Category();
        saved.setCategoryId(2L);
        saved.setCategoryCode("CAT002");
        saved.setCategoryName("Bebidas");
        saved.setDescription("Productos de bebidas");
        saved.setStatus(Category.Status.ACTIVO);

        response.setCategoryId(2L);
        response.setCategoryCode("CAT002");
        response.setCategoryName("Bebidas");
        response.setDescription("Productos de bebidas");

        when(mapper.toEntity(categoryDTO)).thenReturn(category);
        when(repository.save(category)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(response);

        CategoryResponseDTO result = service.createCategory(categoryDTO);

        assertNotNull(result);
        assertEquals(2L, result.getCategoryId());
        assertEquals("CAT002", result.getCategoryCode());
        assertEquals("Bebidas", result.getCategoryName());
        assertEquals(Category.Status.ACTIVO, result.getStatus());

        verify(mapper).toEntity(categoryDTO);
        verify(repository).save(category);
        verify(mapper).toResponseDTO(saved);
    }

    @Test 
    void updateCategory() {
        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(repository.save(category)).thenReturn(category);
        when(mapper.toResponseDTO(category)).thenReturn(response);

        CategoryResponseDTO result = service.updateCategory(1L, updateDTO);

        assertNotNull(result);
        assertEquals(1L, result.getCategoryId());
        assertEquals("CAT001", result.getCategoryCode());
        assertEquals(Category.Status.ACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(mapper).updateEntity(updateDTO, category);
        verify(repository).save(category);
        verify(mapper).toResponseDTO(category);
    }

    @Test 
    void updateCategoryNotExists() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updateCategory(10L, updateDTO));

        assertEquals("La categoria 10 no existe", exception.getMessage());

        verify(repository).findById(10L);
        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusInactive() {
        response.setStatus(Category.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(repository.save(category)).thenReturn(category);
        when(mapper.toResponseDTO(category)).thenReturn(response);

        CategoryResponseDTO result = service.changeStatus(1L, Category.Status.INACTIVO);

        assertNotNull(result);
        assertEquals(1L, result.getCategoryId());
        assertEquals(Category.Status.INACTIVO, category.getStatus());
        assertEquals(Category.Status.INACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(repository).save(category);
        verify(mapper).toResponseDTO(category);
    }

    @Test
    void changeStatusActive() {
        category.setStatus(Category.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(repository.save(category)).thenReturn(category);
        when(mapper.toResponseDTO(category)).thenReturn(response);

        CategoryResponseDTO result = service.changeStatus(1L, Category.Status.ACTIVO);

        assertNotNull(result);
        assertEquals(1L, result.getCategoryId());
        assertEquals(Category.Status.ACTIVO, category.getStatus());
        assertEquals(Category.Status.ACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(repository).save(category);
        verify(mapper).toResponseDTO(category);
    }

    @Test
    void changeStatusWhenAlreadyActive() {
        when(repository.findById(1L)).thenReturn(Optional.of(category));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Category.Status.ACTIVO));

        assertEquals("La categoria ya esta activa", exception.getMessage());

        verify(repository).findById(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWhenAlreadyInactive() {
        category.setStatus(Category.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(category));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Category.Status.INACTIVO));

        assertEquals("La categoria ya esta inactiva", exception.getMessage());

        verify(repository).findById(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWhenCategoryNotExist() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.changeStatus(10L, Category.Status.INACTIVO));

        assertEquals("La categoria 10 no existe", exception.getMessage());

        verify(repository).findById(10L);
        verifyNoInteractions(mapper);
    }
}
