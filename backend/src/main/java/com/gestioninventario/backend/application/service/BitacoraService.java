package com.gestioninventario.backend.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.gestioninventario.backend.domain.entity.Bitacora;
import com.gestioninventario.backend.infrastructure.persistence.repository.BitacoraRepository;

@Service
public class BitacoraService {

    private final BitacoraRepository repository;

    public BitacoraService(BitacoraRepository repository) {
        this.repository = repository;
    }

    public void registrar(String nombreUsuario, String direccionIp, Bitacora.Resultado resultado) {
        Bitacora bitacora = new Bitacora();

        bitacora.setNombre_usuario(nombreUsuario);
        bitacora.setDireccion_ip(direccionIp);
        bitacora.setFecha_hora(LocalDateTime.now());
        bitacora.setResultado(resultado);

        repository.save(bitacora);
    }
}