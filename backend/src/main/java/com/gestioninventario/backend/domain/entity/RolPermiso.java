package com.gestioninventario.backend.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "rol_permiso")
public class RolPermiso {

    @Embeddable
    public static class RolPermisoId implements Serializable {
        
        @Column(name = "id_rol")
        private Long id_rol;

        @Column(name = "id_permiso")
        private Long id_permiso;
        
        public RolPermisoId() {
        }

        public RolPermisoId(Long id_rol, Long id_permiso) {
            this.id_rol = id_rol;
            this.id_permiso = id_permiso;
        }

        public Long getId_rol() {
            return id_rol;
        }

        public void setId_rol(Long id_rol) {
            this.id_rol = id_rol;
        }

        public Long getId_permiso() {
            return id_permiso;
        }

        public void setId_permiso(Long id_permiso) {
            this.id_permiso = id_permiso;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (!(object instanceof RolPermisoId)) {
                return false;
            }

            RolPermisoId that = (RolPermisoId) object;

            return id_rol.equals(that.id_rol) && id_permiso.equals(that.id_permiso);
        }

        @Override
        public int hashCode() {
            return 31 * id_rol.hashCode() + id_permiso.hashCode();
        }
    }

    @EmbeddedId
    private RolPermisoId id;

    @ManyToOne
    @MapsId("id_rol")
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @ManyToOne
    @MapsId("id_permiso")
    @JoinColumn(name = "id_permiso", nullable = false)
    private Permiso permiso;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fecha_asignacion;

    public RolPermiso() {
    }

    public RolPermisoId getId() {
        return id;
    }

    public void setId(RolPermisoId id) {
        this.id = id;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Permiso getPermiso() {
        return permiso;
    }

    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }

    public LocalDateTime getFecha_asignacion() {
        return fecha_asignacion;
    }

    public void setFecha_asignacion(LocalDateTime fecha_asignacion) {
        this.fecha_asignacion = fecha_asignacion;
    }
}