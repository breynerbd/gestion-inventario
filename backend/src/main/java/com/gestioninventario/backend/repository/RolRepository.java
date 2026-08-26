package com.gestioninventario.backend.repository;

import com.gestioninventario.backend.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
}