package com.inventorymanagement.backend.infrastructure.persistence.repository;

import com.inventorymanagement.backend.domain.entity.OperationPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationPermissionRepository
        extends JpaRepository<OperationPermission, String> {
}