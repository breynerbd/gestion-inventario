package com.inventorymanagement.backend.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventorymanagement.backend.application.dto.supplier.SupplierCreateDTO;
import com.inventorymanagement.backend.application.dto.supplier.SupplierStatusDTO;
import com.inventorymanagement.backend.application.dto.supplier.SupplierResponseDTO;
import com.inventorymanagement.backend.application.dto.supplier.SupplierUpdateDTO;
import com.inventorymanagement.backend.application.service.SupplierService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierController.class);

    @GetMapping
    public ResponseEntity<Page<SupplierResponseDTO>> findAll(@PageableDefault(size = 10) Pageable pageable){
        LOGGER.info("Solicitud para obtener todos los proveedores");
        return ResponseEntity.ok(service.findAllSuppliers(pageable));
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierResponseDTO> findById(@PathVariable Long supplierId){
        LOGGER.info("Solicitud para obtener el proveedor con id {}", supplierId);
        return ResponseEntity.ok(service.findSupplierById(supplierId));
    }

    @PostMapping
    public ResponseEntity<SupplierResponseDTO> create(@Valid @RequestBody SupplierCreateDTO supplier){
        LOGGER.info("Solicitud para crear un proveedor");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSupplier(supplier));
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierResponseDTO> update(@PathVariable Long supplierId, @Valid @RequestBody SupplierUpdateDTO supplierDto){
        LOGGER.info("Solicitud para actualizar el proveedor con id {}", supplierId);
        return ResponseEntity.ok(service.updateSupplier(supplierId, supplierDto));
    }

    @PatchMapping("/{supplierId}/status")
    public ResponseEntity<SupplierResponseDTO> changeStatus(@PathVariable Long supplierId, @Valid @RequestBody SupplierStatusDTO statusDto) {
        LOGGER.info("Solicitud para cambiar el estado del proveedor con id {}", supplierId);
        return ResponseEntity.ok(service.changeStatus(supplierId, statusDto.getStatus()));
    }
}