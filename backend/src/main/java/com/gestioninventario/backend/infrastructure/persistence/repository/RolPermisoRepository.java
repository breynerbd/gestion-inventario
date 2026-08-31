package com.gestioninventario.backend.infrastructure.persistence.repository;

import com.gestioninventario.backend.domain.entity.RolPermiso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermiso.RolPermisoId> {
}