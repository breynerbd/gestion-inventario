package com.inventorymanagement.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventorymanagement.backend.domain.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

    boolean existsByCategoryName(String categoryName);

    boolean existsByCategoryNameAndCategoryIdNot(String categoryName, Long categoryId);
}