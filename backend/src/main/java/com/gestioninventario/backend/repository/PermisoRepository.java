package com.gestioninventario.backend.repository;

import com.gestioninventario.backend.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {
}