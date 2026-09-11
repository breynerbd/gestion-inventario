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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestioninventario.backend.application.dto.product.ProductCreateDTO;
import com.gestioninventario.backend.application.dto.product.ProductStatusDTO;
import com.gestioninventario.backend.application.dto.product.ProductResponseDTO;
import com.gestioninventario.backend.application.dto.product.ProductUpdateDTO;
import com.gestioninventario.backend.application.service.ProductService;
import com.gestioninventario.backend.domain.entity.Product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> findAll(
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Product.Status status,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(service.findAllProducts(productCode, productName, categoryId, supplierId, status, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> findAll(@PathVariable Long productId){
        return ResponseEntity.ok(service.findByIdProduct(productId));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductCreateDTO product){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(product));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long productId, @Valid @RequestBody ProductUpdateDTO productDto){
        return ResponseEntity.ok(service.updateProduct(productId, productDto));
    }

    @PatchMapping("/{productId}/status")
    public ResponseEntity<ProductResponseDTO> changeStatus(@PathVariable Long productId, @Valid @RequestBody ProductStatusDTO statusDto) {
        return ResponseEntity.ok(service.changeStatus(productId, statusDto.getStatus()));
    }
}