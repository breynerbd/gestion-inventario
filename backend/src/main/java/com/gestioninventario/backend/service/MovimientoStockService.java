package com.gestioninventario.backend.service;

import com.gestioninventario.backend.entity.MovimientoStock;
import com.gestioninventario.backend.entity.Producto;
import com.gestioninventario.backend.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.repository.MovimientoStockRepository;
import com.gestioninventario.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoStockService {

    private final MovimientoStockRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    public MovimientoStockService(
            MovimientoStockRepository movimientoRepository,
            ProductoRepository productoRepository) {

        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
    }

    public List<MovimientoStock> listarMovimientos() {
        return movimientoRepository.findAll();
    }

    public MovimientoStock obtenerMovimiento(Long id_movimiento) {

        return movimientoRepository.findById(id_movimiento)
            .orElseThrow(() -> new RecursoNoEncontradoException("El movimiento " + id_movimiento + " no existe"));
    }

    public MovimientoStock registrarMovimiento(
            MovimientoStock movimiento) {

        Producto producto = productoRepository.findById(movimiento.getProducto().getId_producto())
            .orElseThrow(() -> new RecursoNoEncontradoException("El producto no existe"));

        if (movimiento.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        if (movimiento.getTipo_movimiento() == MovimientoStock.TipoMovimiento.ENTRADA) {

            producto.setStock_actual(producto.getStock_actual() + movimiento.getCantidad()
            );

        } else if (movimiento.getTipo_movimiento() == MovimientoStock.TipoMovimiento.SALIDA) {

            int nuevoStock = producto.getStock_actual() - movimiento.getCantidad();

            if (nuevoStock < 0) {
                throw new IllegalArgumentException("No hay suficiente stock para realizar la salida");
            }

            producto.setStock_actual(nuevoStock);
        }

        productoRepository.save(producto);
        return movimientoRepository.save(movimiento);
    }

    public MovimientoStock actualizarMovimiento(Long id_movimiento, MovimientoStock movimientoActualizado) {

        MovimientoStock movimiento = movimientoRepository.findById(id_movimiento)
            .orElseThrow(() -> new RecursoNoEncontradoException("El movimiento " + id_movimiento + " no existe"));

        movimiento.setTipo_movimiento(movimientoActualizado.getTipo_movimiento());
        movimiento.setCantidad(movimientoActualizado.getCantidad());
        movimiento.setDocumento_referencia(movimientoActualizado.getDocumento_referencia());
        movimiento.setMotivo(movimientoActualizado.getMotivo());
        movimiento.setProducto(movimientoActualizado.getProducto());
        movimiento.setUsuario(movimientoActualizado.getUsuario());

        return movimientoRepository.save(movimiento);
    }

    public void eliminarMovimiento(Long id_movimiento) {

        MovimientoStock movimiento = movimientoRepository.findById(id_movimiento)
            .orElseThrow(() -> new RecursoNoEncontradoException("El movimiento " + id_movimiento + " no existe"));

        movimientoRepository.delete(movimiento);
    }
}