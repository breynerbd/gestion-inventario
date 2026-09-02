package com.gestioninventario.backend.infrastructure.persistence.repository;

import com.gestioninventario.backend.domain.entity.Rol;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RolRepository extends JpaRepository<Rol, Long> {

    @Query("SELECT r FROM Rol r WHERE r.nombre_rol = :nombreRol")
    Optional<Rol> findByNombreRol(@Param("nombreRol") String nombreRol);

}