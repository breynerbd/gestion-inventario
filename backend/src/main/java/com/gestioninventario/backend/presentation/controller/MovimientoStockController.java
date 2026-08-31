package com.gestioninventario.backend.presentation.controller;

import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockCreateDTO;
import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockResponseDTO;
import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockUpdateDTO;
import com.gestioninventario.backend.application.service.MovimientoStockService;

import jakarta.validation.Valid;

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
    public ResponseEntity<List<MovimientoStockResponseDTO>> listarMovimientos() {
        return ResponseEntity.ok(service.listarMovimientos());
    }

    @GetMapping("/{id_movimiento}")
    public ResponseEntity<MovimientoStockResponseDTO> obtenerMovimientoPorId(@PathVariable("id_movimiento") Long id_movimiento) {

        return ResponseEntity.ok(service.obtenerMovimiento(id_movimiento));
    }

    @PostMapping
    public ResponseEntity<MovimientoStockResponseDTO> registrarMovimiento(@Valid @RequestBody MovimientoStockCreateDTO movimiento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarMovimiento(movimiento));
    }

    @PutMapping("/{id_movimiento}")
    public ResponseEntity<MovimientoStockResponseDTO> actualizarMovimiento(@PathVariable("id_movimiento") Long id_movimiento, @Valid @RequestBody MovimientoStockUpdateDTO movimientoActualizado) {
        return ResponseEntity.ok(service.actualizarMovimiento(id_movimiento, movimientoActualizado));
    }

    @DeleteMapping("/{id_movimiento}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable("id_movimiento") Long id_movimiento) {
        service.eliminarMovimiento(id_movimiento);
        return ResponseEntity.noContent().build();
    }
}