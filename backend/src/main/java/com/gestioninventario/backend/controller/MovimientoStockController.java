package com.gestioninventario.backend.controller;

import com.gestioninventario.backend.entity.MovimientoStock;
import com.gestioninventario.backend.service.MovimientoStockService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoStockController {

    private final MovimientoStockService service;

    public MovimientoStockController(MovimientoStockService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MovimientoStock>> listarMovimientos() {
        return ResponseEntity.ok(service.listarMovimientos());
    }

    @GetMapping("/{id_movimiento}")
    public ResponseEntity<MovimientoStock> obtenerMovimientoPorId(@PathVariable("id_movimiento") Long id_movimiento) {

        return ResponseEntity.ok(service.obtenerMovimiento(id_movimiento));
    }

    @PostMapping
    public ResponseEntity<MovimientoStock> registrarMovimiento(@RequestBody MovimientoStock movimiento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarMovimiento(movimiento));
    }

    @PutMapping("/{id_movimiento}")
    public ResponseEntity<MovimientoStock> actualizarMovimiento(@PathVariable("id_movimiento") Long id_movimiento, @RequestBody MovimientoStock movimientoActualizado) {
        return ResponseEntity.ok(service.actualizarMovimiento(id_movimiento, movimientoActualizado));
    }

    @DeleteMapping("/{id_movimiento}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable("id_movimiento") Long id_movimiento) {
        service.eliminarMovimiento(id_movimiento);
        return ResponseEntity.noContent().build();
    }
}