package com.inventorymanagement.backend.application.dto.binnacle;

import java.time.LocalDateTime;

import com.inventorymanagement.backend.domain.entity.Binnacle.Result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BinnacleResponseDTO {

    private Long logId;
    private String username;
    private LocalDateTime dateTime;
    private Result result;
}
