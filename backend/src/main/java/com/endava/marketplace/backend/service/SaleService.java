package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.dto.SaleByBuyerDTO;
import com.endava.marketplace.backend.dto.SaleBySellerDTO;
import com.endava.marketplace.backend.dto.SaleDTO;
import com.endava.marketplace.backend.mapper.SaleMapper;
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

    private final SaleMapper saleMapper;

    public SaleService(
            SaleRepository saleRepository,
            ListingService listingService,
            SaleStatusService saleStatusService,
            SaleMapper saleMapper
    ) {
        this.saleRepository = saleRepository;
        this.listingService = listingService;
        this.saleStatusService = saleStatusService;
        this.saleMapper = saleMapper;
    }

    public Sale saveSale(Sale sale) {
        if(listingService.updateListingAtSaleCreation(sale.getListing().getId(), sale.getQuantity())) {
            return saleRepository.save(sale);
        }
        return null;
    }

    public SaleDTO findSaleById(Long saleId) {
        return saleRepository.findById(saleId).map(saleMapper::toSaleDTO).orElse(null);
    }

    public Set<SaleByBuyerDTO> findSalesByBuyerId(Long buyerId) {
        return saleMapper.toBuyerDTOSet(saleRepository.findSalesByBuyer_Id(buyerId));
    }

    public Set<SaleBySellerDTO> findSalesBySellerId(Long sellerId) {
        Set<Sale> sales = saleRepository.findSalesByListing_Seller_Id(sellerId);
        return saleMapper.toSellerDTOSet(sales);
    }

    public void updateSaleStatus(Long saleId, String saleStatusName){
        Optional<Sale> foundSale = saleRepository.findById(saleId);
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
