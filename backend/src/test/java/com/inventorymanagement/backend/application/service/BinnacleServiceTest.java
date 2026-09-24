package com.inventorymanagement.backend.application.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inventorymanagement.backend.domain.entity.Binnacle;
import com.inventorymanagement.backend.infrastructure.persistence.repository.BinnacleRepository;

@ExtendWith(MockitoExtension.class)
class BinnacleServiceTest {
    @Mock 
    private BinnacleRepository repository;

    @InjectMocks 
    private BinnacleService service;

    private static final String IP_ADDRESS = "192.0.2.1";

    @Test
    void register() {
        String username = "breynerbd";
        Binnacle.Result result = Binnacle.Result.SUCCESSFUL;
        
        service.register(username, IP_ADDRESS, result);

        verify(repository).save(any(Binnacle.class));
    }
}
