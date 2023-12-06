package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.dto.EndavanDTO;
import com.endava.marketplace.backend.dto.ListingDTO;
import com.endava.marketplace.backend.dto.SaleDTO;
import com.endava.marketplace.backend.event.SaleRatedEvent;
import com.endava.marketplace.backend.mapper.SaleMapper;
import com.endava.marketplace.backend.model.*;
import com.endava.marketplace.backend.repository.SaleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTests {
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Captor
    private ArgumentCaptor<SaleRatedEvent> captor;
    @Mock
    private SaleRepository saleRepository;
    @Mock
    private ListingService listingService;
    @Mock
    private SaleStatusService saleStatusService;
    @Mock
    private SaleMapper saleMapper;

    private SaleService saleService;

    private Endavan buyer;
    private EndavanDTO buyerDTO;
    private Listing listing;
    private ListingDTO listingDTO;
    private SaleStatus saleStatus;

    @BeforeEach
    void setUp() {
        this.saleService = Mockito.spy(new SaleService(eventPublisher, saleRepository, listingService, saleStatusService, saleMapper));

        Endavan seller = Endavan.builder()
                .id(1L)
                .name("Endavan #1")
                .email("endavan1@endava.com")
                .admin(Boolean.FALSE)
                .build();

        EndavanDTO sellerDTO = EndavanDTO.builder()
                .id(seller.getId())
                .name(seller.getName())
                .email(seller.getEmail())
                .build();

        this.buyer = Endavan.builder()
                .id(2L)
                .name("Endavan #2")
                .email("endavan2@endava.com")
                .admin(Boolean.FALSE)
                .build();

        this.buyerDTO = EndavanDTO.builder()
                .id(buyer.getId())
                .name(buyer.getName())
                .email(buyer.getEmail())
                .build();

        ListingCategory category = ListingCategory.builder()
                .id(1L)
                .name("Category #1")
                .active(Boolean.TRUE)
                .build();

        ListingStatus status = ListingStatus.builder()
                .id(1L)
                .name("Status #1")
                .build();

        this.listing = Listing.builder()
                .id(1L)
                .seller(seller)
                .category(category)
                .status(status)
                .name("Listing #1")
                .detail("Detail of Listing #1")
                .price(10000.0)
                .stock(1)
                .condition(10)
                .date(LocalDate.now())
                .build();

        this.listingDTO = ListingDTO.builder()
                .id(listing.getId())
                .seller(sellerDTO)
                .category(listing.getCategory().getName())
                .status(listing.getStatus().getName())
                .name(listing.getName())
                .detail(listing.getDetail())
                .price(listing.getPrice())
                .stock(listing.getStock())
                .condition(listing.getCondition())
                .date(listing.getDate())
                .build();

        this.saleStatus = SaleStatus.builder()
                .id(1L)
                .name("Fulfilled")
                .build();
    }

    @Test
    public void givenSaleIdAndScore_whenRatingSale_thenReturnRatedSaleDTO() {
        //given
        Long id = 1L;
        Integer score = 5;

        Sale sale1 = Sale.builder()
                .id(id)
                .buyer(buyer)
                .listing(listing)
                .status(saleStatus)
                .quantity(1)
                .date(LocalDate.now())
                .build();

        Sale sale2 = Sale.builder()
                .id(sale1.getId())
                .buyer(sale1.getBuyer())
                .listing(sale1.getListing())
                .status(sale1.getStatus())
                .rating(score)
                .quantity(sale1.getQuantity())
                .date(sale1.getDate())
                .build();

        SaleDTO saleDTO = SaleDTO.builder()
                .id(sale2.getId())
                .buyer(buyerDTO)
                .listing(listingDTO)
                .status(sale2.getStatus().getName())
                .rating(sale2.getRating())
                .quantity(sale2.getQuantity())
                .date(sale2.getDate())
                .rating(sale2.getRating())
                .build();

        when(saleRepository.findById(id)).thenReturn(Optional.of(sale1));
        when(saleService.loadSale(id)).thenReturn(sale1);
        when(saleMapper.toSaleDTO(sale2)).thenReturn(saleDTO);

        //when
        SaleDTO test = saleService.rateSale(id, score);

        //then
        Assertions.assertThat(test.getRating()).isNotNull();
        //verify(saleRepository, times(1)).findById(id);
        verify(saleService, times(1)).loadSale(id);
        verify(saleRepository, times(1)).save((sale1));
        verify(eventPublisher, times(1)).publishEvent(captor.capture());
    }

    @Test
    public void givenSaleId_whenLoadingSale_thenReturnSale() {
        //given
        Long id = 1L;

        Sale sale = Sale.builder()
                .id(id)
                .buyer(buyer)
                .listing(listing)
                .status(saleStatus)
                .quantity(1)
                .date(LocalDate.now())
                .build();

        when(saleRepository.findById(id)).thenReturn(Optional.of(sale));

        //when
        Sale test = saleService.loadSale(id);

        //then
        Assertions.assertThat(test).isNotNull();
        verify(saleRepository, times(1)).findById(id);
    }
}
