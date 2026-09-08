package com.gestioninventario.backend.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bitacora")
public class Bitacora {

    public enum Resultado {
        EXITOSO,
        FALLIDO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora")
    private Long id_bitacora;

    @Column(name = "nombre_usuario", nullable = false, length = 50)
    private String nombre_usuario;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fecha_hora;

    @Column(name = "direccion_ip", nullable = false, length = 45)
    private String direccion_ip;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado", nullable = false, length = 10)
    private Resultado resultado;

    public Bitacora() {
    }

    public Long getId_bitacora() {
        return id_bitacora;
    }

    public void setId_bitacora(Long id_bitacora) {
        this.id_bitacora = id_bitacora;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public LocalDateTime getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(LocalDateTime fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getDireccion_ip() {
        return direccion_ip;
    }

    public void setDireccion_ip(String direccion_ip) {
        this.direccion_ip = direccion_ip;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }
}