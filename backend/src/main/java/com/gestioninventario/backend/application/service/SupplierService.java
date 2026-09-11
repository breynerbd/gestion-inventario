package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.domain.entity.Supplier;
import com.gestioninventario.backend.domain.exception.StatusUnchangedException;
import com.gestioninventario.backend.domain.exception.ResourceNotFoundException;
import com.gestioninventario.backend.application.dto.supplier.SupplierCreateDTO;
import com.gestioninventario.backend.application.dto.supplier.SupplierResponseDTO;
import com.gestioninventario.backend.application.dto.supplier.SupplierUpdateDTO;
import com.gestioninventario.backend.application.mapper.SupplierMapper;
import com.gestioninventario.backend.infrastructure.persistence.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor 
public class SupplierService {

    private final SupplierRepository repository;
    private final SupplierMapper mapper;
    private static final String SUPPLIER_NOT_FOUND = "El proveedor ";
    private static final String NOT_EXIST = " no existe";

    public Page<SupplierResponseDTO> findAllSuppliers(Pageable pageable) {

        return repository.findAll(pageable).map(mapper::toResponseDTO);
    }

    public SupplierResponseDTO findSupplierById(Long supplierId) {

        Supplier supplier = repository.findById(supplierId)
            .orElseThrow(() -> new ResourceNotFoundException(SUPPLIER_NOT_FOUND + supplierId + NOT_EXIST));

        return mapper.toResponseDTO(supplier);
    }

    public SupplierResponseDTO createSupplier(SupplierCreateDTO supplierDto) {
        Supplier supplier = mapper.toEntity(supplierDto);
        
        Supplier savedSupplier = repository.save(supplier); 
        
        return mapper.toResponseDTO(savedSupplier);
    }

    public SupplierResponseDTO updateSupplier(Long supplierId, SupplierUpdateDTO supplierDto) {

        Supplier supplier = repository.findById(supplierId)
            .orElseThrow(() -> new ResourceNotFoundException(SUPPLIER_NOT_FOUND + supplierId + NOT_EXIST));
        
        mapper.updateEntity(supplierDto, supplier);

        Supplier updatedSupplier = repository.save(supplier);

        return mapper.toResponseDTO(updatedSupplier);
    }

    public SupplierResponseDTO changeStatus(Long supplierId, Supplier.Status status) {

        Supplier supplier = repository.findById(supplierId)
            .orElseThrow(() ->new ResourceNotFoundException(SUPPLIER_NOT_FOUND + supplierId + NOT_EXIST));

        if (supplier.getStatus() == status) {
            String message = switch (status) {
                case ACTIVO -> "El proveedor ya esta ACTIVO";
                case INACTIVO -> "El proveedor ya esta INACTIVO";
            };

            throw new StatusUnchangedException(message);
        }

        supplier.setStatus(status);

        Supplier updatedSupplier = repository.save(supplier);

        return mapper.toResponseDTO(updatedSupplier);
    }
}