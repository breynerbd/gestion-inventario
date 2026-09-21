package com.inventorymanagement.backend.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.inventorymanagement.backend.application.dto.supplier.SupplierCreateDTO;
import com.inventorymanagement.backend.application.dto.supplier.SupplierResponseDTO;
import com.inventorymanagement.backend.application.dto.supplier.SupplierUpdateDTO;
import com.inventorymanagement.backend.application.mapper.SupplierMapper;
import com.inventorymanagement.backend.domain.entity.Supplier;
import com.inventorymanagement.backend.domain.exception.ResourceNotFoundException;
import com.inventorymanagement.backend.domain.exception.StatusUnchangedException;
import com.inventorymanagement.backend.infrastructure.persistence.repository.SupplierRepository;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock
    private SupplierRepository repository;

    @Mock
    private SupplierMapper mapper;

    @InjectMocks
    private SupplierService service;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setSupplierId(1L);
        supplier.setSupplierCode("PROV001");
        supplier.setBusinessName("Distribuidora");
        supplier.setContactName("Carlos Lopez");
        supplier.setPhone("85967412");
        supplier.setEmail("proveedor@gmail.com");
        supplier.setAddress("Ciudad de Guatemala");
        supplier.setStatus(Supplier.Status.ACTIVO);
    }

    @Test
    void findSupplierById() {
        SupplierResponseDTO response = new SupplierResponseDTO();
        response.setSupplierId(1L);
        response.setSupplierCode("PROV001");
        response.setBusinessName("Distribuidora");
        response.setStatus(Supplier.Status.ACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(supplier));
        when(mapper.toResponseDTO(supplier)).thenReturn(response);

        SupplierResponseDTO result = service.findSupplierById(1L);

        assertNotNull(result);
        assertEquals("PROV001", result.getSupplierCode());
        assertEquals("Distribuidora", result.getBusinessName());

        verify(repository).findById(1L);
        verify(mapper).toResponseDTO(supplier);
    }

    @Test
    void supplierNotFound() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findSupplierById(10L));

        assertEquals("El proveedor 10 no existe", exception.getMessage());

        verify(repository).findById(10L);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAllSuppliers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Supplier> supplierPage = new PageImpl<>(List.of(supplier));

        SupplierResponseDTO response = new SupplierResponseDTO();
        response.setSupplierId(1L);
        response.setSupplierCode("PROV001");
        response.setBusinessName("Distribuidora");

        when(repository.findAll(pageable)).thenReturn(supplierPage);
        when(mapper.toResponseDTO(supplier)).thenReturn(response);

        Page<SupplierResponseDTO> result = service.findAllSuppliers(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("PROV001", result.getContent().get(0).getSupplierCode());

        verify(repository).findAll(pageable);
        verify(mapper).toResponseDTO(supplier);
    }

    @Test
    void createSupplier() {
        SupplierCreateDTO supplierDTO = new SupplierCreateDTO();
        supplierDTO.setSupplierCode("PROV002");
        supplierDTO.setBusinessName("Alimentos Guatemala");
        supplierDTO.setContactName("Juan Perez");
        supplierDTO.setPhone("23694721");
        supplierDTO.setEmail("proveedor2@gmail.com");
        supplierDTO.setAddress("Guatemala");

        Supplier newSupplier = new Supplier();
        newSupplier.setSupplierCode("PROV002");
        newSupplier.setBusinessName("Alimentos Guatemala");
        newSupplier.setStatus(Supplier.Status.ACTIVO);

        Supplier saved = new Supplier();
        saved.setSupplierId(2L);
        saved.setSupplierCode("PROV002");
        saved.setBusinessName("Alimentos Guatemala");
        saved.setStatus(Supplier.Status.ACTIVO);

        SupplierResponseDTO response = new SupplierResponseDTO();
        response.setSupplierId(2L);
        response.setSupplierCode("PROV002");
        response.setBusinessName("Alimentos Guatemala");
        response.setStatus(Supplier.Status.ACTIVO);

        when(mapper.toEntity(supplierDTO)).thenReturn(newSupplier);
        when(repository.save(newSupplier)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(response);

        SupplierResponseDTO result = service.createSupplier(supplierDTO);

        assertNotNull(result);
        assertEquals(2L, result.getSupplierId());
        assertEquals("Alimentos Guatemala", result.getBusinessName());

        verify(mapper).toEntity(supplierDTO);
        verify(repository).save(newSupplier);
        verify(mapper).toResponseDTO(saved);
    }

    @Test
    void updateSupplier() {
        SupplierUpdateDTO updateDTO = new SupplierUpdateDTO();
        updateDTO.setBusinessName("Distribuidora Guate");
        updateDTO.setContactName("Pedro Lopez");
        updateDTO.setPhone("21475869");
        updateDTO.setEmail("proveedor2@gmail.com");
        updateDTO.setAddress("Mixco");

        Supplier updated = new Supplier();
        updated.setSupplierId(1L);
        updated.setSupplierCode("PROV001");
        updated.setBusinessName("Distribuidora Guate");
        updated.setStatus(Supplier.Status.ACTIVO);

        SupplierResponseDTO response = new SupplierResponseDTO();
        response.setSupplierId(1L);
        response.setSupplierCode("PROV001");
        response.setBusinessName("Distribuidora Guate");
        response.setStatus(Supplier.Status.ACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(supplier));
        when(repository.save(supplier)).thenReturn(updated);
        when(mapper.toResponseDTO(updated)).thenReturn(response);

        SupplierResponseDTO result = service.updateSupplier(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Distribuidora Guate", result.getBusinessName());

        verify(repository).findById(1L);
        verify(mapper).updateEntity(updateDTO, supplier);
        verify(repository).save(supplier);
        verify(mapper).toResponseDTO(updated);
    }

    @Test
    void updateSupplierNotFound() {
        SupplierUpdateDTO updateDTO = new SupplierUpdateDTO();
        updateDTO.setBusinessName("Tortrix");

        when(repository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updateSupplier(10L, updateDTO));

        assertEquals("El proveedor 10 no existe", exception.getMessage());

        verify(repository).findById(10L);
        verifyNoInteractions(mapper);
    }

    @Test
    void changeToInactive() {
        Supplier updated = new Supplier();
        updated.setSupplierId(1L);
        updated.setSupplierCode("PROV001");
        updated.setBusinessName("Distribuidora");
        updated.setStatus(Supplier.Status.INACTIVO);

        SupplierResponseDTO response = new SupplierResponseDTO();
        response.setSupplierId(1L);
        response.setSupplierCode("PROV001");
        response.setStatus(Supplier.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(supplier));
        when(repository.save(supplier)).thenReturn(updated);
        when(mapper.toResponseDTO(updated)).thenReturn(response);

        SupplierResponseDTO result = service.changeStatus(1L, Supplier.Status.INACTIVO);

        assertNotNull(result);
        assertEquals(Supplier.Status.INACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(repository).save(supplier);
        verify(mapper).toResponseDTO(updated);
    }

    @Test
    void changeToActive() {
        supplier.setStatus(Supplier.Status.INACTIVO);

        Supplier updated = new Supplier();
        updated.setSupplierId(1L);
        updated.setSupplierCode("PROV001");
        updated.setBusinessName("Distribuidora");
        updated.setStatus(Supplier.Status.ACTIVO);

        SupplierResponseDTO response = new SupplierResponseDTO();
        response.setSupplierId(1L);
        response.setStatus(Supplier.Status.ACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(supplier));
        when(repository.save(supplier)).thenReturn(updated);
        when(mapper.toResponseDTO(updated)).thenReturn(response);

        SupplierResponseDTO result = service.changeStatus(1L, Supplier.Status.ACTIVO);

        assertNotNull(result);
        assertEquals(Supplier.Status.ACTIVO, result.getStatus());

        verify(repository).findById(1L);
        verify(repository).save(supplier);
    }

    @Test
    void changeStatusAlreadyActive() {
        when(repository.findById(1L)).thenReturn(Optional.of(supplier));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Supplier.Status.ACTIVO));

        assertEquals("El proveedor ya esta ACTIVO", exception.getMessage());

        verify(repository).findById(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void changeStatusAlreadyInactive() {
        supplier.setStatus(Supplier.Status.INACTIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(supplier));

        StatusUnchangedException exception = assertThrows(StatusUnchangedException.class, () -> service.changeStatus(1L, Supplier.Status.INACTIVO));

        assertEquals("El proveedor ya esta INACTIVO", exception.getMessage());

        verify(repository).findById(1L);
        verifyNoInteractions(mapper);
    }
}