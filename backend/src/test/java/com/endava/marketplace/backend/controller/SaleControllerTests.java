package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.dto.*;
import com.endava.marketplace.backend.model.*;
import com.endava.marketplace.backend.service.SaleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SaleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SaleControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private SaleService saleService;

    private Endavan buyer;
    private EndavanDTO buyerDTO;
    private Listing listing;
    private ListingDTO listingDTO;
    private SaleStatus saleStatus;

    @BeforeEach
    void setUp() {
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
    public void givenSaleInfo_whenPostSale_thenReturnsSavedSaleDTO() throws Exception {
        // Given
        NewSaleRequestDTO saleRequest = NewSaleRequestDTO.builder()
                .id(1L)
                .buyer_id(buyer.getId())
                .listing_id(listing.getId())
                .quantity(1)
                .build();

        SaleDTO saleDTO = SaleDTO.builder()
                .id(1L)
                .buyer(buyerDTO)
                .listing(listingDTO)
                .status("Pending")
                .quantity(1)
                .build();

        given(saleService.saveSale(saleRequest)).willReturn((saleDTO));

        // When
        ResultActions response = mockMvc.perform(post("/api/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saleRequest)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saleDTO.getId()))
                .andExpect(jsonPath("$.status").value(saleDTO.getStatus()));
        verify(saleService, times(1)).saveSale(saleRequest);
    }

    @Test
    public void givenSaleId_whenGetSaleById_ReturnsSaleDTO() throws Exception {
        // Given
        Long saleId = 1L;

        SaleDTO saleDTO = SaleDTO.builder()
                .id(1L)
                .buyer(buyerDTO)
                .listing(listingDTO)
                .status("Pending")
                .quantity(1)
                .build();

        given(saleService.findSaleById(saleId)).willReturn(saleDTO);

        //When
        ResultActions response = mockMvc.perform(get("/api/sales/{saleId}", saleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saleDTO)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saleDTO.getId()))
                .andExpect(jsonPath("$.status").value(saleDTO.getStatus()))
                .andExpect(jsonPath("$.quantity").value(saleDTO.getQuantity()));
    }

    @Test
    public void givenBuyerId_whenGetSalesByBuyerId_thenReturnListOfSaleDTO() throws Exception {
        // Given
        Long buyerId = 1L;
        boolean sellerFlag = false;

        ListedSaleDTO saleDTO = ListedSaleDTO.builder()
                .id(1L)
                .quantity(1)
                .status("Pending")
                .buyer_name(buyerDTO.getName())
                .build();

        Set<ListedSaleDTO> data = new HashSet<>(List.of(saleDTO));

        given(saleService.findSales(buyerId, sellerFlag)).willReturn(data);

        // When
        ResultActions response = mockMvc.perform(get("/api/sales/buyer/{buyerId}", buyerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(saleDTO.getId()))
                .andExpect(jsonPath("[0].buyer_name").value(saleDTO.getBuyer_name()));
    }

    @Test
    public void givenSellerId_whenGetSalesBySellerId_thenReturnListOfSaleDTO() throws Exception {
        // Given
        Long sellerId = 1L;
        boolean sellerFlag = true;

        ListedSaleDTO saleDTO = ListedSaleDTO.builder()
                .id(1L)
                .quantity(1)
                .status("Pending")
                .seller_name(buyerDTO.getName())
                .build();

        Set<ListedSaleDTO> data = new HashSet<>(List.of(saleDTO));

        given(saleService.findSales(sellerId, sellerFlag)).willReturn(data);

        // When
        ResultActions response = mockMvc.perform(get("/api/sales/seller/{sellerId}", sellerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(saleDTO.getId()))
                .andExpect(jsonPath("[0].seller_name").value(saleDTO.getSeller_name()));
    }

    @Test
    public void givenSaleId_whenCancelSale_thenReturnsSaleDTO() throws Exception {
        // Given
        Long saleId = 1L;
        String saleStatus = "Cancelled";

        SaleDTO saleDTO = SaleDTO.builder()
                .id(saleId)
                .quantity(1)
                .status(saleStatus)
                .buyer(buyerDTO)
                .build();

        given(saleService.updateSaleStatus(saleId, saleStatus)).willReturn(saleDTO);

        // When
        ResultActions response = mockMvc.perform(patch("/api/sales/{saleId}/cancel", saleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saleDTO)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saleId))
                .andExpect(jsonPath("$.status").value(saleStatus));
    }

    @Test
    public void givenSaleId_whenFulfillSale_thenReturnsSaleDTO() throws Exception {
        // Given
        Long saleId = 1L;
        String saleStatus = "Fulfilled";

        SaleDTO saleDTO = SaleDTO.builder()
                .id(saleId)
                .quantity(1)
                .status(saleStatus)
                .buyer(buyerDTO)
                .build();

        given(saleService.updateSaleStatus(saleId, saleStatus)).willReturn(saleDTO);

        // When
        ResultActions response = mockMvc.perform(patch("/api/sales/{saleId}/fulfill", saleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saleDTO)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saleId))
                .andExpect(jsonPath("$.status").value(saleStatus));
    }

    @Test
    public void givenSaleIdAndScore_whenRatingSale_thenReturnRatedSaleDTO() throws Exception {
        // Given
        Long id = 1L;
        Integer score = 5;

        Sale sale = Sale.builder()
                .id(id)
                .buyer(buyer)
                .listing(listing)
                .status(saleStatus)
                .rating(score)
                .quantity(1)
                .date(LocalDate.now())
                .build();

        SaleDTO saleDTO = SaleDTO.builder()
                .id(sale.getId())
                .buyer(buyerDTO)
                .listing(listingDTO)
                .status(sale.getStatus().getName())
                .rating(sale.getRating())
                .quantity(sale.getQuantity())
                .date(sale.getDate())
                .rating(sale.getRating())
                .build();

        given(saleService.rateSale(id, score)).willReturn(saleDTO);

        // When
        ResultActions response = mockMvc.perform(patch("/api/sales/{id}/rate/{rating}", id, score));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.rating").value(score));
        verify(saleService, times(1)).rateSale(id, score);
    }
}
