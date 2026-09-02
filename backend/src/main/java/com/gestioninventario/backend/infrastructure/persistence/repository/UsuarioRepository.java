package com.gestioninventario.backend.infrastructure.persistence.repository;

import com.gestioninventario.backend.domain.entity.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.nombre_usuario = :nombre")
    Optional<Usuario> findByNombreUsuario(@Param("nombre") String nombre);

    @Query("SELECT u FROM Usuario u WHERE u.correo_electronico = :correo")
    Optional<Usuario> findByCorreoElectronico(@Param("correo") String correo);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.nombre_usuario = :nombre")
    boolean existsByCorreoElectronico(@Param("nombre") String nombre);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.nombre_usuario = :nombreUsuario")
    boolean existsByNombreUsuario(@Param("nombreUsuario") String nombreUsuario);
}