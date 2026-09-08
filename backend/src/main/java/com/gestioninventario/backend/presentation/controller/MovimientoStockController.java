package com.gestioninventario.backend.presentation.controller;

import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockCreateDTO;
import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockEstadoDTO;
import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockResponseDTO;
import com.gestioninventario.backend.application.dto.movimiento.MovimientoStockUpdateDTO;
import com.gestioninventario.backend.application.service.MovimientoStockService;
import com.gestioninventario.backend.domain.entity.MovimientoStock;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoStockController {

    private final MovimientoStockService service;

    public MovimientoStockController(MovimientoStockService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<MovimientoStockResponseDTO>> listarMovimientos(
            @RequestParam(name = "id_producto", required = false) Long idProducto,
            @RequestParam(name = "tipo_movimiento", required = false) MovimientoStock.TipoMovimiento tipoMovimiento,
            @RequestParam(name = "fecha_inicio", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(name = "fecha_fin", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(name = "id_usuario", required = false) Long idUsuario,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(service.listarMovimientos(idProducto, tipoMovimiento, fechaInicio, fechaFin, idUsuario,pageable));
    }

    @GetMapping("/{id_movimiento}")
    public ResponseEntity<MovimientoStockResponseDTO> obtenerMovimientoPorId(@PathVariable("id_movimiento") Long id_movimiento) {

        return ResponseEntity.ok(service.obtenerMovimiento(id_movimiento));
    }

    @PostMapping
    public ResponseEntity<MovimientoStockResponseDTO> registrarMovimiento(@Valid @RequestBody MovimientoStockCreateDTO movimiento, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarMovimiento(movimiento, authentication.getName()));
    }

    @PutMapping("/{id_movimiento}")
    public ResponseEntity<MovimientoStockResponseDTO> actualizarMovimiento(@PathVariable("id_movimiento") Long id_movimiento, @Valid @RequestBody MovimientoStockUpdateDTO movimientoActualizado) {
        return ResponseEntity.ok(service.actualizarMovimiento(id_movimiento, movimientoActualizado));
    }

    @PatchMapping("/{id_movimiento}/estado")
    public ResponseEntity<MovimientoStockResponseDTO> cambiarEstado(@PathVariable("id_movimiento") Long id_movimiento, @Valid @RequestBody MovimientoStockEstadoDTO estadoDto) {
        return ResponseEntity.ok(service.cambiarEstado(id_movimiento,estadoDto.getEstado()));
    }
}