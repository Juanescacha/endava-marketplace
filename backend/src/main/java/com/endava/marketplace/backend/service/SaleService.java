package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.Sale;
import com.endava.marketplace.backend.model.SaleStatus;
import com.endava.marketplace.backend.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final SaleStatusService saleStatusService;

    public SaleService(SaleRepository saleRepository, SaleStatusService saleStatusService) {
        this.saleRepository = saleRepository;
        this.saleStatusService = saleStatusService;
    }

    public Sale saveSale(Sale sale) {
        return saleRepository.save(sale);
    }

    public Optional<Sale> findSaleById(Long saleId) {
        return saleRepository.findById(saleId);
    }

    public Optional<Set<Sale>> findSalesByBuyerId(Long buyerId) {
        return saleRepository.findSalesByBuyer_Id(buyerId);
    }

    public Optional<Set<Sale>> findSalesBySellerId(Long sellerId) {
        return saleRepository.findSalesByListing_Seller_Id(sellerId);
    }

    public void updateSaleStatus(Long saleId, Long statusId){
        Optional<Sale> optionalSale = findSaleById(saleId);
        Optional<SaleStatus> optionalStatus = saleStatusService.findSaleStatusById(statusId);
        if(optionalSale.isPresent() && optionalStatus.isPresent()){
            Sale sale = optionalSale.get();
            SaleStatus status = optionalStatus.get();
            sale.setStatus(status);
            saleRepository.save(sale);
        }
    }
}
