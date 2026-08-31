package com.gestioninventario.backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    public enum TipoDocumento {
        NIT,
        RUC,
        CEDULA,
        PASAPORTE
    }

    public enum Estado {
        ACTIVO,
        INACTIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long id_proveedor;

    @Column(name = "codigo_proveedor", nullable = false, unique = true, length = 10)
    private String codigo_proveedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 15)
    private TipoDocumento tipo_documento;

    @Column(name = "numero_documento", nullable = false, unique = true, length = 20)
    private String numero_documento;

    @Column(name = "razon_social", nullable = false, length = 150)
    private String razon_social;

    @Column(name = "nombre_contacto", length = 100)
    private String nombre_contacto;

    @Column(name = "telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 100)
    private String correo_electronico;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private Estado estado;

    public Proveedor() {
    }

    public Long getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(Long id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public String getCodigo_proveedor() {
        return codigo_proveedor;
    }

    public void setCodigo_proveedor(String codigo_proveedor) {
        this.codigo_proveedor = codigo_proveedor;
    }

    public TipoDocumento getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(TipoDocumento tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getNumero_documento() {
        return numero_documento;
    }

    public void setNumero_documento(String numero_documento) {
        this.numero_documento = numero_documento;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getNombre_contacto() {
        return nombre_contacto;
    }

    public void setNombre_contacto(String nombre_contacto) {
        this.nombre_contacto = nombre_contacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}