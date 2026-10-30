package com.inventorymanagement.backend.application.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.inventorymanagement.backend.application.dto.binnacle.BinnacleResponseDTO;
import com.inventorymanagement.backend.application.mapper.BinnacleMapper;
import com.inventorymanagement.backend.domain.entity.Binnacle;
import com.inventorymanagement.backend.infrastructure.persistence.repository.BinnacleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BinnacleService {

    private static final ZoneId ZONE_ID = ZoneId.of("America/Guatemala");
    private final BinnacleRepository repository;
    private final BinnacleMapper mapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(BinnacleService.class);

    public List<BinnacleResponseDTO> findAllLogs() {
        LOGGER.debug("Obteniendo datos de bitacora");
        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public void register(String username, String ipAddress, Binnacle.Result result) {
        Binnacle binnacle = new Binnacle();

        binnacle.setUsername(username);
        binnacle.setIpAddress(ipAddress);
        binnacle.setDateTime(LocalDateTime.now(ZONE_ID));
        binnacle.setResult(result);

        LOGGER.info("Intento de inicio de sesion registrado");

        repository.save(binnacle);
    }
}