package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.dto.EndavanDTO;
import com.endava.marketplace.backend.dto.ListingDTO;
import com.endava.marketplace.backend.dto.SaleDTO;
import com.endava.marketplace.backend.model.*;
import com.endava.marketplace.backend.service.SaleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

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
    public void givenSaleIdAndScore_whenRatingSale_thenReturnRatedSaleDTO() throws Exception {
        //given
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

        //when
        ResultActions response = mockMvc.perform(patch("/api/sales/{id}/rate/{rating}", id, score));

        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.rating").value(score));
        verify(saleService, times(1)).rateSale(id, score);
    }
}
