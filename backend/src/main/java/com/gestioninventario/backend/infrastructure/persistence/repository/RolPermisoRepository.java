package com.gestioninventario.backend.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gestioninventario.backend.domain.entity.RolPermiso;

public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermiso.RolPermisoId> {

    @Query("SELECT rp FROM RolPermiso rp WHERE rp.rol.id_rol = :id_rol")
    List<RolPermiso> findPermisosByRol(@Param("id_rol") Long id_rol);

    @Modifying
    @Query("DELETE FROM RolPermiso rp WHERE rp.rol.id_rol = :id_rol")
    void deletePermisosByRol(@Param("id_rol") Long id_rol);
}