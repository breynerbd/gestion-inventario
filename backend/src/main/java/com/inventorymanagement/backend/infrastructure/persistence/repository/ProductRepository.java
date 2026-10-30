package com.inventorymanagement.backend.infrastructure.persistence.repository;

import com.inventorymanagement.backend.domain.entity.Product;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query ("SELECT p FROM Product p WHERE p.status = :status AND p.currentStock <= p.minimumStock")
    List<Product> findLowStockProducts(@Param ("status") Product.Status status);

    @Query("select p.category.categoryName, count(p) from Product p where p.status = :status and p.currentStock > 0 group by p.category.categoryName")
    List<Object[]> findProductCountInCategory(@Param("status") Product.Status status);

    long countProductsByStatus(Product.Status status);

    boolean existsByCategory_CategoryId(Long categoryId);

    boolean existsByCategory_CategoryIdAndStatus(Long categoryId, Product.Status status);

    boolean existsBySupplier_SupplierIdAndStatus(Long supplierId, Product.Status status);

    boolean existsBySupplier_SupplierId(Long supplierId);

    boolean existsByProductName(String productName);

    boolean existsByProductNameAndProductIdNot(String productName, Long productId);

    @Query("select coalesce(sum(p.currentStock * p.salePrice), 0) from Product p where p.status = :status")
    BigDecimal calculateInventoryValue(@Param("status") Product.Status status);

    @Query("select coalesce(sum(p.currentStock * p.purchasePrice), 0) from Product p")
    BigDecimal calculateInventoryInvestment();
}