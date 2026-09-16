package com.inventorymanagement.backend.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
    }

    @Test 
    void findCategoryById() {
        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setCategoryId(1L);
        response.setCategoryCode("CAT001");
        response.setCategoryName("Embutidos");
        response.setDescription("Carnes procesadas frescas");
        response.setStatus(Category.Status.ACTIVO);

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

        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setCategoryId(1L);
        response.setCategoryCode("CAT001");
        response.setCategoryName("Embutidos");
        response.setDescription("Carnes procesadas frescas");
        response.setStatus(Category.Status.ACTIVO);

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
        CategoryCreateDTO categoryDTO = new CategoryCreateDTO();
        categoryDTO.setCategoryCode("CAT002");
        categoryDTO.setCategoryName("Bebidas");
        categoryDTO.setDescription("Productos de bebidas");

        category = new Category();
        category.setCategoryCode("CAT002");
        category.setCategoryName("Bebidas");
        category.setDescription("Productos de bebidas");

        Category saved = new Category();
        saved.setCategoryId(2L);
        saved.setCategoryCode("CAT002");
        saved.setCategoryName("Bebidas");
        saved.setDescription("Productos de bebidas");
        saved.setStatus(Category.Status.ACTIVO);

        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setCategoryId(2L);
        response.setCategoryCode("CAT002");
        response.setCategoryName("Bebidas");
        response.setDescription("Productos de bebidas");
        response.setStatus(Category.Status.ACTIVO);

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
        CategoryUpdateDTO updateDTO = new CategoryUpdateDTO();
        updateDTO.setCategoryName("Bebidas Frias");
        updateDTO.setDescription("Productos de bebidas frias");

        category = new Category();
        category.setCategoryId(2L);
        category.setCategoryCode("CAT002");
        category.setCategoryName("Bebidas");
        category.setDescription("Productos de bebidas");
        category.setStatus(Category.Status.ACTIVO);

        Category update = new Category();
        update.setCategoryId(2L);
        update.setCategoryCode("CAT002");
        update.setCategoryName("Bebidas frias");
        update.setDescription("Productos de bebidas frias");
        update.setStatus(Category.Status.ACTIVO);
        
        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setCategoryId(2L);
        response.setCategoryCode("CAT002");
        response.setCategoryName("Bebidas frias");
        response.setDescription("Productos de bebidas frias");
        response.setStatus(Category.Status.ACTIVO);

        when(repository.findById(2L)).thenReturn(Optional.of(category));
        when(repository.save(category)).thenReturn(update);
        when(mapper.toResponseDTO(update)).thenReturn(response);

        CategoryResponseDTO result = service.updateCategory(2L, updateDTO);

        assertNotNull(result);
        assertEquals(2L, result.getCategoryId());
        assertEquals("CAT002", result.getCategoryCode());
        assertEquals("Bebidas frias", result.getCategoryName());
        assertEquals("Productos de bebidas frias", result.getDescription());
        assertEquals(Category.Status.ACTIVO, result.getStatus());

        verify(repository).findById(2L);
        verify(mapper).updateEntity(updateDTO, category);
        verify(repository).save(category);
        verify(mapper).toResponseDTO(update);
    }

    @Test 
    void updateCategoryNotExists() {
        CategoryUpdateDTO updateDTO = new CategoryUpdateDTO();
        updateDTO.setCategoryName("Bebidas Calientes");
        updateDTO.setDescription("Productos de bebidas calientes");

        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updateCategory(10L, updateDTO));

        assertEquals("La categoria 10 no existe", exception.getMessage());

        verify(repository).findById(10L);
        verify(repository, never()).save(any(Category.class));
        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusInactive() {
        category = new Category();
        category.setCategoryId(2L);
        category.setCategoryCode("CAT002");
        category.setCategoryName("Bebidas");
        category.setDescription("Productos de bebidas");
        category.setStatus(Category.Status.ACTIVO);

        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(2L);
        updatedCategory.setCategoryCode("CAT002");
        updatedCategory.setCategoryName("Bebidas");
        updatedCategory.setDescription("Productos de bebidas");
        updatedCategory.setStatus(Category.Status.INACTIVO);

        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setCategoryId(2L);
        response.setCategoryCode("CAT002");
        response.setCategoryName("Bebidas");
        response.setDescription("Productos de bebidas");
        response.setStatus(Category.Status.INACTIVO);

        when(repository.findById(2L)).thenReturn(Optional.of(category));
        when(repository.save(category)).thenReturn(updatedCategory);
        when(mapper.toResponseDTO(updatedCategory)).thenReturn(response);

        CategoryResponseDTO result = service.changeStatus(2L, Category.Status.INACTIVO);

        assertNotNull(result);
        assertEquals(2L, result.getCategoryId());
        assertEquals(Category.Status.INACTIVO, category.getStatus());
        assertEquals(Category.Status.INACTIVO, result.getStatus());

        verify(repository).findById(2L);
        verify(repository).save(category);
        verify(mapper).toResponseDTO(updatedCategory);
    }

    @Test
    void changeStatusActive() {
        category = new Category();
        category.setCategoryId(2L);
        category.setCategoryCode("CAT002");
        category.setCategoryName("Bebidas");
        category.setDescription("Productos de bebidas");
        category.setStatus(Category.Status.INACTIVO);

        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(2L);
        updatedCategory.setCategoryCode("CAT002");
        updatedCategory.setCategoryName("Bebidas");
        updatedCategory.setDescription("Productos de bebidas");
        updatedCategory.setStatus(Category.Status.ACTIVO);

        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setCategoryId(2L);
        response.setCategoryCode("CAT002");
        response.setCategoryName("Bebidas");
        response.setDescription("Productos de bebidas");
        response.setStatus(Category.Status.ACTIVO);

        when(repository.findById(2L)).thenReturn(Optional.of(category));
        when(repository.save(category)).thenReturn(updatedCategory);
        when(mapper.toResponseDTO(updatedCategory)).thenReturn(response);

        CategoryResponseDTO result = service.changeStatus(2L, Category.Status.ACTIVO);

        assertNotNull(result);
        assertEquals(2L, result.getCategoryId());
        assertEquals(Category.Status.ACTIVO, category.getStatus());
        assertEquals(Category.Status.ACTIVO, result.getStatus());

        verify(repository).findById(2L);
        verify(repository).save(category);
        verify(mapper).toResponseDTO(updatedCategory);
    }

    @Test
    void changeStatusWhenAlreadyActive() {
        category = new Category();
        category.setCategoryId(2L);
        category.setCategoryCode("CAT002");
        category.setCategoryName("Bebidas");
        category.setDescription("Productos de bebidas");
        category.setStatus(Category.Status.ACTIVO);

        when(repository.findById(2L)).thenReturn(Optional.of(category));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(2L, Category.Status.ACTIVO));

        assertEquals("La categoria ya esta activa", exception.getMessage());

        verify(repository).findById(2L);
        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusWhenAlreadyInactive() {
        category = new Category();
        category.setCategoryId(2L);
        category.setCategoryCode("CAT002");
        category.setCategoryName("Bebidas");
        category.setDescription("Productos de bebidas");
        category.setStatus(Category.Status.INACTIVO);

        when(repository.findById(2L)).thenReturn(Optional.of(category));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(2L, Category.Status.INACTIVO));

        assertEquals("La categoria ya esta inactiva", exception.getMessage());

        verify(repository).findById(2L);
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
