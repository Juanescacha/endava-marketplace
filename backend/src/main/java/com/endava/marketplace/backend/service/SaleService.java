package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.dto.NewSaleRequestDTO;
import com.endava.marketplace.backend.dto.SaleByBuyerDTO;
import com.endava.marketplace.backend.dto.SaleBySellerDTO;
import com.endava.marketplace.backend.dto.SaleDTO;
import com.endava.marketplace.backend.exception.EntityNotFoundException;
import com.endava.marketplace.backend.mapper.SaleMapper;
import com.endava.marketplace.backend.model.Sale;
import com.endava.marketplace.backend.model.SaleStatus;
import com.endava.marketplace.backend.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public SaleDTO saveSale(NewSaleRequestDTO newSaleRequestDTO) {
        Sale sale = saleMapper.toSale(newSaleRequestDTO);
        sale.setStatus(saleStatusService.getSaleStatuses().get("Pending"));
        sale.setDate(LocalDate.now());
        listingService.updateListingAtSaleCreation(sale.getListing().getId(), sale.getQuantity());
        return saleMapper.toSaleDTO(saleRepository.save(sale));
    }

    public SaleDTO findSaleById(Long id) {
        Optional<Sale> foundSale = saleRepository.findById(id);
        if (foundSale.isEmpty()) {
            throw new EntityNotFoundException("Sale with id " + id + " wasn't found");
        }
        return saleMapper.toSaleDTO(foundSale.get());
    }

    public Set<SaleByBuyerDTO> findSalesByBuyerId(Long id) {
        return saleMapper.toBuyerDTOSet(saleRepository.findSalesByBuyer_Id(id));
    }

    public Set<SaleBySellerDTO> findSalesBySellerId(Long id) {
        return saleMapper.toSellerDTOSet(saleRepository.findSalesByListing_Seller_Id(id));
    }

    public SaleDTO updateSaleStatus(Long id, String newSaleStatus) {
        Optional<Sale> foundSale = saleRepository.findById(id);

        if (foundSale.isEmpty()) {
            throw new EntityNotFoundException("Sale with id " + id + " wasn't found");
        }

        Sale sale = foundSale.get();
        SaleStatus saleStatus = saleStatusService.getSaleStatuses().get(newSaleStatus);

        if(newSaleStatus.equals("Cancelled")) {
            listingService.updateListingAtSaleCancellation(sale.getListing().getId(), sale.getQuantity());
        }

        sale.setStatus(saleStatus);
        return saleMapper.toSaleDTO(saleRepository.save(sale));
    }
}
