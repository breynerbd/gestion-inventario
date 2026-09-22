package com.inventorymanagement.backend.application.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.inventorymanagement.backend.domain.entity.Binnacle;
import com.inventorymanagement.backend.infrastructure.persistence.repository.BinnacleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BinnacleService {

    private static final ZoneId ZONE_ID = ZoneId.of("America/Guatemala");
    private final BinnacleRepository repository;
    private static final Logger LOGGER = LoggerFactory.getLogger(BinnacleService.class);

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