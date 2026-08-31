package com.gestioninventario.backend.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.dto.movimiento.MovimientoStockCreateDTO;
import com.gestioninventario.backend.dto.movimiento.MovimientoStockResponseDTO;
import com.gestioninventario.backend.dto.movimiento.MovimientoStockUpdateDTO;
import com.gestioninventario.backend.entity.MovimientoStock;
import com.gestioninventario.backend.entity.Producto;
import com.gestioninventario.backend.entity.Usuario;

@Component
public class MovimientoStockMapper {

    public MovimientoStock toEntity(MovimientoStockCreateDTO dto, Producto producto, Usuario usuario) {

        MovimientoStock movimiento = new MovimientoStock();

        movimiento.setTipo_movimiento(dto.getTipo_movimiento());
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setDocumento_referencia(dto.getDocumento_referencia());
        movimiento.setMotivo(dto.getMotivo());
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
        movimiento.setFecha_movimiento(LocalDateTime.now());

        return movimiento;
    }

    public void updateEntity(MovimientoStockUpdateDTO dto, MovimientoStock movimiento, Producto producto, Usuario usuario) {

        movimiento.setTipo_movimiento(dto.getTipo_movimiento());
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setDocumento_referencia(dto.getDocumento_referencia());
        movimiento.setMotivo(dto.getMotivo());     
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
    }

    public MovimientoStockResponseDTO toResponseDTO(
            MovimientoStock movimiento) {

        MovimientoStockResponseDTO dto = new MovimientoStockResponseDTO();

        dto.setId_movimiento(movimiento.getId_movimiento());
        dto.setTipo_movimiento(movimiento.getTipo_movimiento());
        dto.setCantidad(movimiento.getCantidad());
        dto.setDocumento_referencia(movimiento.getDocumento_referencia());
        dto.setMotivo(movimiento.getMotivo());
        dto.setFecha_movimiento(movimiento.getFecha_movimiento());

        if (movimiento.getProducto() != null) {
            dto.setId_producto(
                    movimiento.getProducto().getId_producto()
            );

            dto.setNombre_producto(
                    movimiento.getProducto().getNombre_producto()
            );
        }

        if (movimiento.getUsuario() != null) {
            dto.setId_usuario(
                    movimiento.getUsuario().getId_usuario()
            );

            dto.setNombre_usuario(
                    movimiento.getUsuario().getNombre_usuario()
            );
        }

        return dto;
    }
}