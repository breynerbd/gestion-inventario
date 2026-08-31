package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockCreateDTO;
import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockResponseDTO;
import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockUpdateDTO;
import com.gestioninventario.backend.domain.entity.MovimientoStock;
import com.gestioninventario.backend.domain.entity.Producto;
import com.gestioninventario.backend.domain.entity.Usuario;
import com.gestioninventario.backend.domain.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.application.mapper.MovimientoStockMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.MovimientoStockRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.ProductoRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoStockService {

    private final MovimientoStockRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoStockMapper mapper;

    public MovimientoStockService(MovimientoStockRepository movimientoRepository, ProductoRepository productoRepository, UsuarioRepository usuarioRepository, MovimientoStockMapper mapper) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    public List<MovimientoStockResponseDTO> listarMovimientos() {
        return movimientoRepository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public MovimientoStockResponseDTO obtenerMovimiento(Long id_movimiento) {

        MovimientoStock movimiento = movimientoRepository.findById(id_movimiento)
            .orElseThrow(() -> new RecursoNoEncontradoException("El movimiento " + id_movimiento + " no existe"));

        return mapper.toResponseDTO(movimiento);
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

        MovimientoStock movimiento = mapper.toEntity(movimientoDto, producto, usuario);

        MovimientoStock movimientoGuardado = movimientoRepository.save(movimiento);

        return mapper.toResponseDTO(movimientoGuardado);
    }

    public MovimientoStockResponseDTO actualizarMovimiento(Long id_movimiento, MovimientoStockUpdateDTO movimientoDto) {

        MovimientoStock movimiento = movimientoRepository.findById(id_movimiento)
            .orElseThrow(() -> new RecursoNoEncontradoException("El movimiento " + id_movimiento + " no existe"));

        Producto productoAnterior = movimiento.getProducto();

        if (movimiento.getTipo_movimiento() == MovimientoStock.TipoMovimiento.ENTRADA) {

            productoAnterior.setStock_actual(
                    productoAnterior.getStock_actual() - movimiento.getCantidad()
            );

        } else if (movimiento.getTipo_movimiento() == MovimientoStock.TipoMovimiento.SALIDA) {

            productoAnterior.setStock_actual(
                    productoAnterior.getStock_actual() + movimiento.getCantidad()
            );
        }

        Producto nuevoProducto = productoRepository.findById(movimientoDto.getId_producto())
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + movimientoDto.getId_producto() + " no existe"));

        Usuario usuario = usuarioRepository.findById(movimientoDto.getId_usuario())
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + movimientoDto.getId_usuario() + " no existe"));

        if (movimientoDto.getTipo_movimiento() == MovimientoStock.TipoMovimiento.ENTRADA) {

            nuevoProducto.setStock_actual(
                    nuevoProducto.getStock_actual() + movimientoDto.getCantidad()
            );

        } else if (movimientoDto.getTipo_movimiento() == MovimientoStock.TipoMovimiento.SALIDA) {

            int nuevoStock = nuevoProducto.getStock_actual() - movimientoDto.getCantidad();

            if (nuevoStock < 0) {
                throw new IllegalArgumentException("No hay suficiente stock para realizar la salida");
            }

            nuevoProducto.setStock_actual(nuevoStock);
        }

        productoRepository.save(productoAnterior);

        if (!productoAnterior.getId_producto().equals(nuevoProducto.getId_producto())) {
            productoRepository.save(nuevoProducto);
        }

        mapper.updateEntity(movimientoDto, movimiento, nuevoProducto, usuario);

        MovimientoStock movimientoActualizado = movimientoRepository.save(movimiento);

        return mapper.toResponseDTO(movimientoActualizado);
    }

    public void eliminarMovimiento(Long id_movimiento) {

    MovimientoStock movimiento = movimientoRepository.findById(id_movimiento)
        .orElseThrow(() -> new RecursoNoEncontradoException("El movimiento " + id_movimiento + " no existe"));

    Producto producto = movimiento.getProducto();

    if (movimiento.getTipo_movimiento() == MovimientoStock.TipoMovimiento.ENTRADA) {

        producto.setStock_actual(
                producto.getStock_actual() - movimiento.getCantidad()
        );

    } else if (movimiento.getTipo_movimiento() == MovimientoStock.TipoMovimiento.SALIDA) {

        producto.setStock_actual(
                producto.getStock_actual() + movimiento.getCantidad()
        );
    }

    productoRepository.save(producto);
    movimientoRepository.delete(movimiento);
}
}