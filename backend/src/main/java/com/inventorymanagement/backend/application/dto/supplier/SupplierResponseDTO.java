package com.inventorymanagement.backend.application.dto.supplier;

import java.time.LocalDateTime;

import com.inventorymanagement.backend.domain.entity.Supplier.DocumentType;
import com.inventorymanagement.backend.domain.entity.Supplier.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class SupplierResponseDTO {
    
    private Long supplierId;
    private String supplierCode;
    private DocumentType documentType;
    private String documentNumber;
    private String businessName;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private Status status;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
}