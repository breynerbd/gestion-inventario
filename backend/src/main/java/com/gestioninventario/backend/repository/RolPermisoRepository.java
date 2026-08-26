package com.gestioninventario.backend.repository;

import com.gestioninventario.backend.entity.RolPermiso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermiso.RolPermisoId> {
}