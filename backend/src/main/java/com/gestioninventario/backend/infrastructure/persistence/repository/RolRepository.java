package com.gestioninventario.backend.infrastructure.persistence.repository;

import com.gestioninventario.backend.domain.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
}