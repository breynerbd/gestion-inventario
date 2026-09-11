package com.gestioninventario.backend.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestioninventario.backend.domain.entity.RolePermission;

public interface RolePermissionRepository
        extends JpaRepository<RolePermission, RolePermission.RolePermissionId> {

    List<RolePermission> findByRoleRoleId(Long roleId);

    void deleteByRoleRoleId(Long roleId);
}