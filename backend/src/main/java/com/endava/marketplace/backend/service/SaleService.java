package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.Sale;
import com.endava.marketplace.backend.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class SaleService {
    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public Sale saveSale(Sale sale) {
        return saleRepository.save(sale);
    }

    public Optional<Sale> findSaleById(Long saleId) {
        return saleRepository.findById(saleId.intValue());
    }

    public Optional<Set<Sale>> findSalesByBuyerId(Long buyerId) {
        return saleRepository.findSalesByBuyer_Id(buyerId);
    }

    public Optional<Set<Sale>> findSalesBySellerId(Long sellerId) {
        return saleRepository.findSalesByListing_Seller_Id(sellerId);
    }
}
