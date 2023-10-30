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

    private final ListingService listingService;

    private final SaleStatusService saleStatusService;

    public SaleService(SaleRepository saleRepository, ListingService listingService, SaleStatusService saleStatusService) {
        this.saleRepository = saleRepository;
        this.listingService = listingService;
        this.saleStatusService = saleStatusService;
    }

    public Sale saveSale(Sale sale) {
        if(listingService.updateListingAtSaleCreation(sale.getListing().getId(), sale.getQuantity())) {
            return saleRepository.save(sale);
        }
        return null;
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

    public void updateSaleStatus(Long saleId, String saleStatusName){
        Optional<Sale> foundSale = findSaleById(saleId);
        Optional<SaleStatus> foundSaleStatus = saleStatusService.findSaleStatusByName(saleStatusName);

        if(foundSale.isPresent() && foundSaleStatus.isPresent()) {
            Sale sale = foundSale.get();
            SaleStatus saleStatus = foundSaleStatus.get();

            if(saleStatus.getName().equals("Cancelled")) {
                listingService.updateListingAtSaleCancellation(sale.getListing().getId(), sale.getQuantity());
            }

            sale.setStatus(saleStatus);
            saleRepository.save(sale);
        }

    }
}
