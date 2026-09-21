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

    @Test
    void register() {
        String username = "breynerbd";
        String ipAddress = "192.168.1.10";
        Binnacle.Result result = Binnacle.Result.SUCCESSFUL;
        
        service.register(username, ipAddress, result);

        verify(repository).save(any(Binnacle.class));
    }
}
