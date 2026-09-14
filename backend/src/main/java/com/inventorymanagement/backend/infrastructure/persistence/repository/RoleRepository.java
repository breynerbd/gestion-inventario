package com.inventorymanagement.backend.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventorymanagement.backend.domain.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String roleName);
}