package com.inventorymanagement.backend.infrastructure.persistence.repository;

import com.inventorymanagement.backend.domain.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    long countSupplierByStatus(Supplier.Status status);

    boolean existsByBusinessName(String businessName);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByDocumentNumber(String documentNumber);

    boolean existsByBusinessNameAndSupplierIdNot(String businessName, Long supplierId);

    boolean existsByEmailAndSupplierIdNot(String email, Long supplierId);

    boolean existsByPhoneAndSupplierIdNot(String phone, Long supplierId);

    boolean existsByDocumentNumberAndSupplierIdNot(String documentNumber, Long supplierId);
}