package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.SaleStatus;
import com.endava.marketplace.backend.repository.SaleStatusRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostLoad;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SaleStatusService {
    private final SaleStatusRepository saleStatusRepository;

    @Getter
    private Map<String, SaleStatus> saleStatuses;

    public SaleStatusService(SaleStatusRepository saleStatusRepository) {
        this.saleStatusRepository = saleStatusRepository;
    }

    @PostConstruct
    @PostLoad
    private void getAllSaleStatuses() {
        saleStatuses = saleStatusRepository.findAll().stream()
                .collect(Collectors.toMap(SaleStatus::getName, Function.identity()));
    }
}
