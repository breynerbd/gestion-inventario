package com.inventorymanagement.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.inventorymanagement.backend.application.dto.binnacle.BinnacleResponseDTO;
import com.inventorymanagement.backend.domain.entity.Binnacle;

@Component 
public class BinnacleMapper {
    
    public BinnacleResponseDTO toResponseDTO(Binnacle binnacle) {
        BinnacleResponseDTO dto = new BinnacleResponseDTO();

        dto.setLogId(binnacle.getLogId());
        dto.setUsername(binnacle.getUsername());
        dto.setDateTime(binnacle.getDateTime());
        dto.setResult(binnacle.getResult());

        return dto;
    }
}
