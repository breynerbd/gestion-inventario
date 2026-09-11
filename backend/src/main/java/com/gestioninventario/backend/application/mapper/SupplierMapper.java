package com.gestioninventario.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.gestioninventario.backend.application.dto.supplier.SupplierCreateDTO;
import com.gestioninventario.backend.application.dto.supplier.SupplierResponseDTO;
import com.gestioninventario.backend.application.dto.supplier.SupplierUpdateDTO;
import com.gestioninventario.backend.domain.entity.Supplier;

@Component
public class SupplierMapper {

    public Supplier toEntity(SupplierCreateDTO dto) {

        Supplier supplier = new Supplier();

        supplier.setSupplierCode(dto.getSupplierCode());
        supplier.setDocumentType(dto.getDocumentType());
        supplier.setDocumentNumber(dto.getDocumentNumber());
        supplier.setBusinessName(dto.getBusinessName());
        supplier.setContactName(dto.getContactName());
        supplier.setPhone(dto.getPhone());
        supplier.setEmail(dto.getEmail());
        supplier.setAddress(dto.getAddress());
        supplier.setStatus(Supplier.Status.ACTIVO);

        return supplier;
    }

    public void updateEntity(SupplierUpdateDTO dto, Supplier supplier) {

        supplier.setDocumentType(dto.getDocumentType());
        supplier.setDocumentNumber(dto.getDocumentNumber());
        supplier.setBusinessName(dto.getBusinessName());
        supplier.setContactName(dto.getContactName());
        supplier.setPhone(dto.getPhone());
        supplier.setEmail(dto.getEmail());
        supplier.setAddress(dto.getAddress());
    }

    public SupplierResponseDTO toResponseDTO(Supplier supplier) {

        SupplierResponseDTO dto = new SupplierResponseDTO();

        dto.setSupplierId(supplier.getSupplierId());
        dto.setSupplierCode(supplier.getSupplierCode());
        dto.setDocumentType(supplier.getDocumentType());
        dto.setDocumentNumber(supplier.getDocumentNumber());
        dto.setBusinessName(supplier.getBusinessName());
        dto.setContactName(supplier.getContactName());
        dto.setPhone(supplier.getPhone());
        dto.setEmail(supplier.getEmail());
        dto.setAddress(supplier.getAddress());
        dto.setStatus(supplier.getStatus());

        return dto;
    }
}