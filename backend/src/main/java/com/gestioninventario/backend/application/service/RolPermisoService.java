package com.gestioninventario.backend.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestioninventario.backend.application.dto.rolPermiso.RolPermisoResponseDTO;
import com.gestioninventario.backend.application.dto.rolPermiso.RolPermisoDTO;
import com.gestioninventario.backend.application.mapper.RolPermisoMapper;
import com.gestioninventario.backend.domain.entity.Permiso;
import com.gestioninventario.backend.domain.entity.Rol;
import com.gestioninventario.backend.domain.entity.RolPermiso;
import com.gestioninventario.backend.domain.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.infrastructure.persistence.repository.PermisoRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.RolPermisoRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.RolRepository;

@Service
public class RolPermisoService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final RolPermisoMapper rolPermisoMapper;

    public RolPermisoService(RolRepository rolRepository, PermisoRepository permisoRepository, RolPermisoRepository rolPermisoRepository, RolPermisoMapper rolPermisoMapper) {
        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
        this.rolPermisoRepository = rolPermisoRepository;
        this.rolPermisoMapper = rolPermisoMapper;
    }

    public List<RolPermisoResponseDTO> listarPermisosRol(Long id_rol) {
        Rol rol = rolRepository.findById(id_rol)
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + id_rol + " no existe"));

        return rolPermisoRepository.findPermisosByRol(rol.getId_rol()).stream().map(rolPermisoMapper::toResponseDTO).toList();
    }

    @Transactional
    public List<RolPermisoResponseDTO> asignarPermisos(Long id_rol, RolPermisoDTO dto) {

        Rol rol = rolRepository.findById(id_rol)
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + id_rol + " no existe"));

        if (rol.getEstado() == Rol.Estado.INACTIVO) {
            throw new IllegalArgumentException("No se pueden asignar permisos a un rol inactivo");
        }

        List<Long> permisosSinDuplicados = dto.getPermisos().stream().distinct().toList();

        List<Permiso> permisos = permisosSinDuplicados.stream()
            .map(id_permiso -> permisoRepository.findById(id_permiso)
                .orElseThrow(() -> new RecursoNoEncontradoException("El permiso " + id_permiso + " no existe"))).toList();

        for (Permiso permiso : permisos) {
            if (permiso.getEstado() == Permiso.Estado.INACTIVO) {
                throw new IllegalArgumentException("El permiso " + permiso.getId_permiso() + " esta inactivo");
            }
        }

        rolPermisoRepository.deletePermisosByRol(id_rol);

        for (Permiso permiso : permisos) {

            RolPermiso relacion = new RolPermiso();

            relacion.setId(new RolPermiso.RolPermisoId(rol.getId_rol(),permiso.getId_permiso()));

            relacion.setRol(rol);
            relacion.setPermiso(permiso);
            relacion.setFecha_asignacion(LocalDateTime.now());

            rolPermisoRepository.save(relacion);
        }

        return listarPermisosRol(id_rol);
    }
}