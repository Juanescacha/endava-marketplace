package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.SaleStatus;
import com.endava.marketplace.backend.repository.SaleStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class SaleStatusService {
    private final SaleStatusRepository saleStatusRepository;

    public SaleStatusService(SaleStatusRepository saleStatusRepository) {
        this.saleStatusRepository = saleStatusRepository;
    }

    public List<SaleStatus> findAllSaleStatus(){
        return saleStatusRepository.findAll();
    }

    public Optional<SaleStatus> findSaleStatusById(Long saleStatusId) {
        return saleStatusRepository.findById(saleStatusId);
    }
}
