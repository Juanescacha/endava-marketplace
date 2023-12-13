package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.dto.*;
import com.endava.marketplace.backend.event.SaleRatedEvent;
import com.endava.marketplace.backend.exception.EntityAttributeAlreadySetException;
import com.endava.marketplace.backend.exception.EntityNotFoundException;
import com.endava.marketplace.backend.exception.InvalidStatusException;
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
import java.util.*;

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
    public void givenSaleInfo_whenSaveSale_thenReturnSavedSale(){
        // Given
        Long saleId = 1L;
        SaleDTO saleDto = SaleDTO.builder()
                .id(saleId)
                .quantity(1)
                .date(LocalDate.now())
                .build();

        Sale sale = Sale.builder()
                .id(saleId)
                .listing(listing)
                .buyer(buyer)
                .quantity(1)
                .date(LocalDate.now())
                .build();

        NewSaleRequestDTO saleRequestDto = NewSaleRequestDTO.builder()
                .id(1L)
                .listing_id(listing.getId())
                .buyer_id(buyer.getId())
                .quantity(1)
                .build();

        SaleStatus pending = SaleStatus.builder()
                .id(1L)
                .name("Pending")
                .build();

        Map<String, SaleStatus> statuses = new HashMap<>();
        statuses.put("Pending", pending);

        when(saleMapper.toSale(saleRequestDto)).thenReturn(sale);
        when(saleStatusService.getSaleStatuses()).thenReturn(statuses);
        when(saleRepository.save(sale)).thenReturn(sale);
        when(saleMapper.toSaleDTO(sale)).thenReturn(saleDto);

        // When
        SaleDTO savedSale = saleService.saveSale(saleRequestDto);

        // Then
        verify(listingService, times(1)).updateListingAtSaleCreation(sale.getListing().getId(), sale.getQuantity());
        Assertions.assertThat(savedSale).isEqualTo(saleDto);
    }

    @Test
    public void givenSaleId_whenFindSaleById_thenReturnsSaleDTO(){
        // Given
        Long saleId = 1L;
        Sale sale = Sale.builder()
                .id(saleId)
                .buyer(buyer)
                .listing(listing)
                .quantity(0)
                .build();

        SaleDTO saleDto = SaleDTO.builder()
                .id(saleId)
                .buyer(buyerDTO)
                .listing(listingDTO)
                .quantity(0)
                .build();

        when(saleRepository.findById(saleId)).thenReturn(Optional.ofNullable(sale));
        when(saleMapper.toSaleDTO(sale)).thenReturn(saleDto);

        // When
        SaleDTO foundSale = saleService.findSaleById(saleId);

        // Then
        Assertions.assertThat(foundSale).isNotNull();
        Assertions.assertThat(foundSale).isEqualTo(saleDto);
    }

    @Test
    public void givenWrongSaleId_whenFindSaleById_thenReturnsEntityNotFoundException(){
        // Given
        Long saleId = 1L;

        when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThatThrownBy(
                        () -> saleService.findSaleById(saleId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Sale with id " + saleId + " wasn't found");
    }

    @Test
    public void givenEndavanIdAndSellerFlag_whenFindSales_thenReturnsSales(){
        Long id = 1L;
        boolean isSeller = false;

        Sale saleOne = Sale.builder()
               .id(1L)
               .buyer(buyer)
               .listing(listing)
               .quantity(1)
               .build();

       Sale saleTwo = Sale.builder()
               .id(2L)
               .buyer(buyer)
               .listing(listing)
               .quantity(1)
               .build();
       Set<Sale> data = new HashSet<>(List.of(saleOne, saleTwo));

       ListedSaleDTO saleOneDto = ListedSaleDTO.builder()
                .id(1L)
                .seller_name(listing.getSeller().getName())
                .buyer_name(buyer.getName())
                .quantity(1)
                .build();

       ListedSaleDTO saleTwoDto = ListedSaleDTO.builder()
               .id(2L)
               .seller_name(listing.getSeller().getName())
               .buyer_name(buyer.getName())
               .quantity(1)
               .build();

       Set<ListedSaleDTO> expectedResults = new HashSet<>(List.of(saleOneDto, saleTwoDto));

       when(saleRepository.findSalesByBuyer_Id(buyer.getId())).thenReturn(data);
       when(saleMapper.toListedSaleDTOSet(data)).thenReturn(expectedResults);

       //When
       Set<ListedSaleDTO> foundSales = saleService.findSales(buyer.getId(), isSeller);

       // Then
       verify(saleRepository, times(1)).findSalesByBuyer_Id(buyer.getId());
       Assertions.assertThat(foundSales).isEqualTo(expectedResults);

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
        verify(saleService, times(1)).loadSale(id);
        verify(saleRepository, times(1)).save((sale1));
        verify(eventPublisher, times(1)).publishEvent(captor.capture());
    }

    @Test
    public void givenSaleIdAndScore_whenRatingSale_andSaleAlreadyRated_thenReturnEntityAttributeAlreadySetException() {
        //given
        Long saleId = 1L;
        Integer score = 5;

        Sale sale = Sale.builder()
                .id(saleId)
                .buyer(buyer)
                .listing(listing)
                .status(saleStatus)
                .rating(score)
                .quantity(1)
                .date(LocalDate.now())
                .build();

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(saleService.loadSale(saleId)).thenReturn(sale);

        // When - Then
        Assertions.assertThatThrownBy(
                ()-> saleService.rateSale(saleId, score))
                .isInstanceOf(EntityAttributeAlreadySetException.class)
                .hasMessageContaining("Sale with ID: " + saleId + " was already rated");
    }

    @Test
    public void givenSaleIdAndScore_whenRatingSale_andSaleIsPending_thenReturnInvalidStatusException() {
        //given
        Long saleId = 1L;
        Integer score = 5;

        SaleStatus pending = SaleStatus.builder()
                .id(1L)
                .name("Pending")
                .build();

        Map<String, SaleStatus> statuses = new HashMap<>();
        statuses.put("Pending", pending);

        Sale sale = Sale.builder()
                .id(saleId)
                .buyer(buyer)
                .listing(listing)
                .status(pending)
                .quantity(1)
                .date(LocalDate.now())
                .build();

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(saleService.loadSale(saleId)).thenReturn(sale);
        when(saleStatusService.getSaleStatuses()).thenReturn(statuses);

        // When - Then
        Assertions.assertThatThrownBy(
                        ()-> saleService.rateSale(saleId, score))
                .isInstanceOf(InvalidStatusException.class)
                .hasMessageContaining("Sale with ID: " + saleId + " is pending and cannot be rated");
    }

    @Test
    public void givenSaleIdAndStatus_whenUpdateSaleStatus_thenReturnsUpdatedSaleDTO(){
        // Given
        Long saleId = 1L;
        String saleStatus = "Cancelled";

        SaleStatus pending = SaleStatus.builder()
                .id(1L)
                .name("Pending")
                .build();

        SaleStatus cancelled = SaleStatus.builder()
                .id(2L)
                .name("Cancelled")
                .build();
        Map<String, SaleStatus> statuses = new HashMap<>();
        statuses.put("Pending", pending);
        statuses.put("Cancelled", cancelled);

        Sale sale = Sale.builder()
                .id(saleId)
                .status(pending)
                .buyer(buyer)
                .listing(listing)
                .quantity(1)
                .build();

        Sale sale2 = Sale.builder()
                .id(saleId)
                .status(cancelled)
                .buyer(buyer)
                .listing(listing)
                .quantity(1)
                .build();

        SaleDTO saleDto = SaleDTO.builder()
                .id(saleId)
                .status("Fulfilled")
                .listing(listingDTO)
                .quantity(1)
                .build();

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(saleService.loadSale(saleId)).thenReturn(sale);
        when(saleStatusService.getSaleStatuses()).thenReturn(statuses);
        when(saleRepository.save(sale)).thenReturn(sale2);
        when(saleMapper.toSaleDTO(sale2)).thenReturn(saleDto);

        // When
        SaleDTO updatedSale = saleService.updateSaleStatus(saleId, saleStatus);

        // Then
        Assertions.assertThat(updatedSale).isNotNull();
        Assertions.assertThat(updatedSale).isEqualTo(saleDto);
        verify(listingService, times(1)).updateListingAtSaleCancellation(sale.getListing().getId(), sale.getQuantity());
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
    @Test
    public void givenWrongSaleId_whenLoadingSale_thenReturnEntityNotFoundException() {
        // Given
        Long saleId = 1L;

        when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThatThrownBy(
                        () -> saleService.loadSale(saleId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Sale with id " + saleId + " wasn't found");
    }
}
