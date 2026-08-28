package com.gestioninventario.backend.service;

import com.gestioninventario.backend.dto.movimiento.MovimientoStockCreateDTO;
import com.gestioninventario.backend.dto.movimiento.MovimientoStockResponseDTO;
import com.gestioninventario.backend.dto.movimiento.MovimientoStockUpdateDTO;
import com.gestioninventario.backend.entity.MovimientoStock;
import com.gestioninventario.backend.entity.Producto;
import com.gestioninventario.backend.entity.Usuario;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.repository.MovimientoStockRepository;
import com.gestioninventario.backend.repository.ProductoRepository;
import com.gestioninventario.backend.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoStockService {

    private final MovimientoStockRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public MovimientoStockService(MovimientoStockRepository movimientoRepository, ProductoRepository productoRepository, UsuarioRepository usuarioRepository) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<MovimientoStockResponseDTO> listarMovimientos() {
        return movimientoRepository.findAll().stream().map(this::movimientoResponse).toList();
    }

    public MovimientoStockResponseDTO obtenerMovimiento(Long id_movimiento) {

        MovimientoStock movimiento = movimientoRepository.findById(id_movimiento)
            .orElseThrow(() -> new RecursoNoEncontradoException("El movimiento " + id_movimiento + " no existe"));

        return movimientoResponse(movimiento);
    }

    public MovimientoStockResponseDTO registrarMovimiento(MovimientoStockCreateDTO movimientoDto) {

        Producto producto = productoRepository.findById(movimientoDto.getId_producto())
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + movimientoDto.getId_producto() + " no existe"));

        Usuario usuario = usuarioRepository.findById(movimientoDto.getId_usuario())
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + movimientoDto.getId_usuario() + " no existe"));


        if (movimientoDto.getTipo_movimiento() == MovimientoStock.TipoMovimiento.ENTRADA) {

            producto.setStock_actual(producto.getStock_actual() + movimientoDto.getCantidad());

        } else if (movimientoDto.getTipo_movimiento() == MovimientoStock.TipoMovimiento.SALIDA) {

            int nuevoStock = producto.getStock_actual() - movimientoDto.getCantidad();

            if (nuevoStock < 0) {
                throw new IllegalArgumentException("No hay suficiente stock para realizar la salida");
            }

            producto.setStock_actual(nuevoStock);
        }


        productoRepository.save(producto);

        MovimientoStock movimiento = new MovimientoStock();

        movimiento.setTipo_movimiento(movimientoDto.getTipo_movimiento());
        movimiento.setProducto(producto);
        movimiento.setCantidad(movimientoDto.getCantidad());
        movimiento.setDocumento_referencia(movimientoDto.getDocumento_referencia());
        movimiento.setMotivo(movimientoDto.getMotivo());
        movimiento.setUsuario(usuario);
        movimiento.setFecha_movimiento(LocalDateTime.now());

        MovimientoStock movimientoGuardado = movimientoRepository.save(movimiento);

        return movimientoResponse(movimientoGuardado);
    }

    public MovimientoStockResponseDTO actualizarMovimiento(Long id_movimiento, MovimientoStockUpdateDTO movimientoDto) {

        MovimientoStock movimiento = movimientoRepository.findById(id_movimiento)
            .orElseThrow(() -> new RecursoNoEncontradoException("El movimiento " + id_movimiento + " no existe"));

        Producto producto = productoRepository.findById(movimientoDto.getId_producto())
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + movimientoDto.getId_producto() + " no existe"));

        Usuario usuario = usuarioRepository.findById(movimientoDto.getId_usuario())
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + movimientoDto.getId_usuario() + " no existe"));

        movimiento.setTipo_movimiento(movimientoDto.getTipo_movimiento());
        movimiento.setProducto(producto);
        movimiento.setCantidad(movimientoDto.getCantidad());
        movimiento.setDocumento_referencia(movimientoDto.getDocumento_referencia());
        movimiento.setMotivo(movimientoDto.getMotivo());
        movimiento.setUsuario(usuario);

        MovimientoStock movimientoActualizado = movimientoRepository.save(movimiento);

        return movimientoResponse(movimientoActualizado);
    }

    public void eliminarMovimiento(Long id_movimiento) {

        MovimientoStock movimiento = movimientoRepository.findById(id_movimiento)
            .orElseThrow(() -> new RecursoNoEncontradoException("El movimiento " + id_movimiento + " no existe"));

        movimientoRepository.delete(movimiento);
    }

    private MovimientoStockResponseDTO movimientoResponse(MovimientoStock movimiento) {

        MovimientoStockResponseDTO dto = new MovimientoStockResponseDTO();

        dto.setId_movimiento(movimiento.getId_movimiento());
        dto.setTipo_movimiento(movimiento.getTipo_movimiento());

        if (movimiento.getProducto() != null) {
            dto.setId_producto(
                    movimiento.getProducto().getId_producto()
            );

            dto.setNombre_producto(
                    movimiento.getProducto().getNombre_producto()
            );
        }

        dto.setCantidad(movimiento.getCantidad());
        dto.setDocumento_referencia(
                movimiento.getDocumento_referencia()
        );
        dto.setMotivo(movimiento.getMotivo());

        if (movimiento.getUsuario() != null) {
            dto.setId_usuario(
                    movimiento.getUsuario().getId_usuario()
            );

            dto.setNombre_usuario(
                    movimiento.getUsuario().getNombre_usuario()
            );
        }

        dto.setFecha_movimiento(
                movimiento.getFecha_movimiento()
        );

        return dto;
    }
}