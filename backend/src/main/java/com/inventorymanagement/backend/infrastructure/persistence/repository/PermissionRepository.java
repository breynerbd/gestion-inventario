package com.inventorymanagement.backend.infrastructure.persistence.repository;

import com.inventorymanagement.backend.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}