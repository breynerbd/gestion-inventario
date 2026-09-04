package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockCreateDTO;
import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockResponseDTO;
import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockUpdateDTO;
import com.gestioninventario.backend.domain.entity.MovimientoStock;
import com.gestioninventario.backend.domain.entity.Producto;
import com.gestioninventario.backend.domain.entity.Usuario;
import com.gestioninventario.backend.domain.exception.EstadoSinCambiosException;
import com.gestioninventario.backend.domain.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.application.mapper.MovimientoStockMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.MovimientoStockRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.ProductoRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private void validarMotivo(MovimientoStock.TipoMovimiento tipoMovimiento, String motivo) {
        if (tipoMovimiento == MovimientoStock.TipoMovimiento.SALIDA
                || tipoMovimiento == MovimientoStock.TipoMovimiento.AJUSTE_POSITIVO
                || tipoMovimiento == MovimientoStock.TipoMovimiento.AJUSTE_NEGATIVO) {

            if (motivo == null || motivo.isBlank()) {
                throw new IllegalArgumentException("El motivo es obligatorio para este tipo de movimiento");
            }
        }
    }

    public List<MovimientoStockResponseDTO> listarMovimientos() {
        return movimientoRepository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public MovimientoStockResponseDTO obtenerMovimiento(Long id_movimiento) {

        MovimientoStock movimiento = movimientoRepository.findById(id_movimiento)
            .orElseThrow(() -> new RecursoNoEncontradoException("El movimiento " + id_movimiento + " no existe"));

        return mapper.toResponseDTO(movimiento);
    }

    @Transactional
    public MovimientoStockResponseDTO registrarMovimiento(MovimientoStockCreateDTO movimientoDto) {
        validarMotivo(movimientoDto.getTipo_movimiento(), movimientoDto.getMotivo());

        Producto producto = productoRepository.findById(movimientoDto.getId_producto())
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + movimientoDto.getId_producto() + " no existe"));

        Usuario usuario = usuarioRepository.findById(movimientoDto.getId_usuario())
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + movimientoDto.getId_usuario() + " no existe"));

        MovimientoStock movimiento = mapper.toEntity(movimientoDto, producto, usuario);

        aplicarMovimiento(movimiento, producto);

        productoRepository.save(producto);

        MovimientoStock movimientoGuardado = movimientoRepository.save(movimiento);

        return mapper.toResponseDTO(movimientoGuardado);
    }

    @Transactional
    public MovimientoStockResponseDTO actualizarMovimiento(Long id_movimiento, MovimientoStockUpdateDTO movimientoDto) {
        validarMotivo(movimientoDto.getTipo_movimiento(), movimientoDto.getMotivo());

        MovimientoStock movimiento = movimientoRepository.findById(id_movimiento)
            .orElseThrow(() -> new RecursoNoEncontradoException("El movimiento " + id_movimiento + " no existe"));

        Producto productoAnterior = movimiento.getProducto();

        Producto nuevoProducto = productoRepository.findById(movimientoDto.getId_producto())
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto " + movimientoDto.getId_producto() + " no existe"));

        Usuario usuario = usuarioRepository.findById(movimientoDto.getId_usuario())
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario " + movimientoDto.getId_usuario() + " no existe"));

        if (movimiento.getEstado() == MovimientoStock.Estado.ACTIVO) {

            revertirMovimiento(movimiento, productoAnterior);

            MovimientoStock movimientoNuevo = new MovimientoStock();
            movimientoNuevo.setTipo_movimiento(movimientoDto.getTipo_movimiento());
            movimientoNuevo.setCantidad(movimientoDto.getCantidad());

            if (productoAnterior.getId_producto().equals(nuevoProducto.getId_producto())) {

                aplicarMovimiento(movimientoNuevo, productoAnterior);

                productoRepository.save(productoAnterior);

            } else {

                aplicarMovimiento(movimientoNuevo, nuevoProducto);

                productoRepository.save(productoAnterior);
                productoRepository.save(nuevoProducto);
            }
        }

        mapper.updateEntity(movimientoDto, movimiento, nuevoProducto, usuario);

        MovimientoStock movimientoActualizado = movimientoRepository.save(movimiento);

        return mapper.toResponseDTO(movimientoActualizado);
    }

    private void aplicarMovimiento(MovimientoStock movimiento, Producto producto) {

        switch (movimiento.getTipo_movimiento()) {
            case ENTRADA, AJUSTE_POSITIVO -> {
                producto.setStock_actual(producto.getStock_actual() + movimiento.getCantidad());
            }

            case SALIDA, AJUSTE_NEGATIVO -> {
                int nuevoStock = producto.getStock_actual() - movimiento.getCantidad();

                if (nuevoStock < 0) {
                    throw new IllegalArgumentException("No hay suficiente stock para realizar el movimiento");
                }

                producto.setStock_actual(nuevoStock);
            }
        }
    }

    private void revertirMovimiento(MovimientoStock movimiento, Producto producto) {

        switch (movimiento.getTipo_movimiento()) {
            case ENTRADA, AJUSTE_POSITIVO -> {
                int nuevoStock = producto.getStock_actual() - movimiento.getCantidad();

                if (nuevoStock < 0) {
                    throw new IllegalArgumentException("No es posible inactivar el movimiento porque el stock quedaria negativo");
                }

                producto.setStock_actual(nuevoStock);
            }

            case SALIDA, AJUSTE_NEGATIVO -> {
                producto.setStock_actual(producto.getStock_actual() + movimiento.getCantidad());
            }
        }
    }

    @Transactional
    public MovimientoStockResponseDTO cambiarEstado(Long id_movimiento,MovimientoStock.Estado estado) {

        MovimientoStock movimiento = movimientoRepository.findById(id_movimiento)
            .orElseThrow(() -> new RecursoNoEncontradoException("El movimiento " + id_movimiento + " no existe"));

        if (movimiento.getEstado() == estado) {
            String mensaje = switch (estado) {
                case ACTIVO -> "El movimiento ya esta activo";
                case INACTIVO -> "El movimiento ya esta inactivo";
            };

            throw new EstadoSinCambiosException(mensaje);
        }

        Producto producto = movimiento.getProducto();

        if (estado == MovimientoStock.Estado.INACTIVO) {
            revertirMovimiento(movimiento, producto);
        } else {
            aplicarMovimiento(movimiento, producto);
        }

        movimiento.setEstado(estado);

        productoRepository.save(producto);

        MovimientoStock movimientoActualizado = movimientoRepository.save(movimiento);

        return mapper.toResponseDTO(movimientoActualizado);
    }
}