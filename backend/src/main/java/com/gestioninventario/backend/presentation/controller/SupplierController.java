package com.gestioninventario.backend.presentation.controller;

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

import com.gestioninventario.backend.application.dto.supplier.SupplierCreateDTO;
import com.gestioninventario.backend.application.dto.supplier.SupplierStatusDTO;
import com.gestioninventario.backend.application.dto.supplier.SupplierResponseDTO;
import com.gestioninventario.backend.application.dto.supplier.SupplierUpdateDTO;
import com.gestioninventario.backend.application.service.SupplierService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/supplier")
public class SupplierController {

    private final SupplierService service;

    @GetMapping
    public ResponseEntity<Page<SupplierResponseDTO>> findAll(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(service.findAllSuppliers(pageable));
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierResponseDTO> findById(@PathVariable Long supplierId){
        return ResponseEntity.ok(service.findSupplierById(supplierId));
    }

    @PostMapping
    public ResponseEntity<SupplierResponseDTO> create(@Valid @RequestBody SupplierCreateDTO supplier){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSupplier(supplier));
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierResponseDTO> update(@PathVariable Long supplierId, @Valid @RequestBody SupplierUpdateDTO supplierDto){
        return ResponseEntity.ok(service.updateSupplier(supplierId, supplierDto));
    }

    @PatchMapping("/{supplierId}/status")
    public ResponseEntity<SupplierResponseDTO> changeStatus(@PathVariable Long supplierId, @Valid @RequestBody SupplierStatusDTO statusDto) {
        return ResponseEntity.ok(service.changeStatus(supplierId, statusDto.getStatus()));
    }
}