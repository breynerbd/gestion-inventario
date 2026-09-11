package com.gestioninventario.backend.application.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.gestioninventario.backend.domain.entity.Binnacle;
import com.gestioninventario.backend.infrastructure.persistence.repository.BinnacleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BinnacleService {

    private static final ZoneId ZONE_ID = ZoneId.of("America/Guatemala");
    private final BinnacleRepository repository;

    public void register(String username, String ipAddress, Binnacle.Result result) {
        Binnacle binnacle = new Binnacle();

        binnacle.setUsername(username);
        binnacle.setIpAddress(ipAddress);
        binnacle.setDateTime(LocalDateTime.now(ZONE_ID));
        binnacle.setResult(result);

        repository.save(binnacle);
    }
}