package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.model.Sale;
import com.endava.marketplace.backend.model.SaleStatus;
import com.endava.marketplace.backend.repository.SaleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTests {
    @Mock
    private SaleRepository saleRepository;
    @Mock
    private SaleStatusService saleStatusService;

    private SaleService saleService;

    @BeforeEach
    void setUp(){
        saleService = new SaleService(saleRepository, saleStatusService);
    }

    @Test
    public void givenSaleInfo_whenSaveSale_thenReturnsSavedSale(){
        Sale sale = Sale.builder()
                .id(1L)
                .buyer(null)
                .listing(null)
                .status(null)
                .date(LocalDate.of(2023,10,9))
                .build();

        when(saleRepository.save(Mockito.any(Sale.class))).thenReturn(sale);

        Sale savedSale = saleService.saveSale(sale);

        Assertions.assertThat(savedSale).isNotNull();
        Assertions.assertThat(savedSale.getId()).isGreaterThan(0);
        verify(saleRepository, times(1)).save(sale);
    }

    @Test
    public void givenSaleId_whenFindSaleById_ThenReturnsSale(){
        Long saleId = 1L;

        Sale sale = Sale.builder()
                .id(1L)
                .buyer(null)
                .listing(null)
                .status(null)
                .date(LocalDate.of(2023,10,9))
                .build();

        when(saleRepository.findById(saleId.intValue())).thenReturn(Optional.ofNullable(sale));
        Optional<Sale> foundSale = saleService.findSaleById(saleId);

        Assertions.assertThat(foundSale).isNotNull();
        Assertions.assertThat(sale.getId()).isEqualTo(foundSale.get().getId());
    }

    @Test
    public void givenBuyerId_whenFindSalesByBuyerId_thenReturnsSalesThatMatch(){
        Set<Sale> sales = new HashSet<>();
        Long buyerId = 1L;

        Endavan buyer = Endavan.builder()
                .id(1L)
                .name("Endavan")
                .email("example@email.com")
                .admin(false)
                .build();

        Sale saleOne = Sale.builder()
                .id(1L)
                .buyer(buyer)
                .date(LocalDate.of(2023,10,9))
                .build();
        Sale saleTwo = Sale.builder()
                .id(2L)
                .buyer(buyer)
                .date(LocalDate.of(2023,10,9))
                .build();

        sales.add(saleOne);
        sales.add(saleTwo);

        when(saleRepository.findSalesByBuyer_Id(buyerId)).thenReturn(Optional.of(sales));
        Optional<Set<Sale>> foundSales = saleService.findSalesByBuyerId(buyerId);

        Assertions.assertThat(foundSales).isNotNull();
        Assertions.assertThat(foundSales.get().size()).isEqualTo(2);
    }

    @Test
    public void givenSellerId_whenFindSalesByBuyerId_thenReturnsSalesThatMatch(){
        Long sellerId = 1L;
        Set<Sale> sales = new HashSet<>();

        Endavan seller = Endavan.builder()
                .id(1L)
                .name("Endavan")
                .email("example@email.com")
                .admin(false)
                .build();

        Listing listing = Listing.builder()
                .id(1L)
                .seller(seller)
                .name("listing #1")
                .detail("ExampleDetail")
                .price(10000.0)
                .stock(1)
                .condition(9)
                .date(LocalDate.of(2023, 10, 9))
                .build();

        Sale saleOne = Sale.builder()
                .id(1L)
                .listing(listing)
                .date(LocalDate.of(2023,10,9))
                .build();

        sales.add(saleOne);

        when(saleRepository.findSalesByListing_Seller_Id(sellerId)).thenReturn(Optional.of(sales));
        Optional<Set<Sale>> foundSales = saleService.findSalesBySellerId(sellerId);

        Assertions.assertThat(foundSales).isNotNull();
        Assertions.assertThat(foundSales.get().size()).isEqualTo(1);
    }

    @Test
    public void givenNewSaleStatusId_whenUpdateSaleStatus_thenStatusIsUpdated(){
        Long saleId = 1L;
        Long statusId = 1L;
        Optional<Sale> optionalSale = Optional.ofNullable(Mockito.mock(Sale.class));
        Optional<SaleStatus> optionalStatus = Optional.ofNullable(Mockito.mock(SaleStatus.class));

        when(saleService.findSaleById(1L)).thenReturn(optionalSale);
        when(saleStatusService.findSaleStatusById(1L)).thenReturn(optionalStatus);

        saleService.updateSaleStatus(saleId, statusId);

        verify(saleRepository,times(1)).save(optionalSale.get());
    }
}
