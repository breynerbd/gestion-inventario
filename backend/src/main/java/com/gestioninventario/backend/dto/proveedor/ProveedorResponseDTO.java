package com.gestioninventario.backend.dto.proveedor;

import com.gestioninventario.backend.entity.Proveedor;

public class ProveedorResponseDTO {

    private Long id_proveedor;
    private String codigo_proveedor;
    private Proveedor.TipoDocumento tipo_documento;
    private String numero_documento;
    private String razon_social;
    private String nombre_contacto;
    private String telefono;
    private String correo_electronico;
    private String direccion;
    private Proveedor.Estado estado;

    public ProveedorResponseDTO() {
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

    public Proveedor.TipoDocumento getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(Proveedor.TipoDocumento tipo_documento) {
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

    public Proveedor.Estado getEstado() {
        return estado;
    }

    public void setEstado(Proveedor.Estado estado) {
        this.estado = estado;
    }
}