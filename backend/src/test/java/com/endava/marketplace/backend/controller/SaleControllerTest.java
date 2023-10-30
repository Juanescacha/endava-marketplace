package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.model.Sale;
import com.endava.marketplace.backend.service.SaleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
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
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SaleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SaleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SaleService saleService;

    @Test
    public void givenSaleInfo_whenSaveSale_thenReturnsCreatedSale() throws Exception{
        Sale sale = Sale.builder()
                .id(1L)
                .date(LocalDate.of(2023,10,12))
                .build();

        given(saleService.saveSale(any(Sale.class))).
                willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/api/sales/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sale)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(sale.getId().intValue())))
                .andExpect(jsonPath("$.date", CoreMatchers.is((sale.getDate()).toString())));
    }

    @Test
    public void givenSaleId_whenGetSaleById_ReturnsSale() throws Exception {
        Long saleId = 1L;

        Sale sale = Sale.builder()
                .id(1L)
                .date(LocalDate.of(2023,10,12))
                .build();

        given(saleService.findSaleById(saleId)).willReturn(Optional.of(sale));

        ResultActions response = mockMvc.perform(get("/api/sales/get/{saleId}", saleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sale)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sale.getId().intValue())))
                .andExpect(jsonPath("$.date", is(sale.getDate().toString())));
    }

    @Test
    public void givenBuyerId_whenGetSalesByBuyerId_thenReturnSalesThatMatch() throws Exception {
        Long buyerId = 1L;

        Set<Sale> sales = new HashSet<>();
        Endavan buyer = Endavan.builder()
                .id(buyerId)
                .name("Endavan")
                .email("email@example.com")
                .build();
        Sale sale = Sale.builder()
                .id(1L)
                .buyer(buyer)
                .date(LocalDate.of(2023,10,12))
                .build();
        sales.add(sale);

        given(saleService.findSalesByBuyerId(buyerId)).willReturn(Optional.of(sales));

        ResultActions response = mockMvc.perform(get("/api/sales/get/buyer/{buyerId}", buyerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sales)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", is(sale.getId().intValue())))
                .andExpect(jsonPath("[0].buyer.id", is(buyer.getId().intValue())));
    }

    @Test
    public void givenSellerId_whenGetSalesBySellerId_thenReturnSalesThatMatch() throws Exception {
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
        Sale sale = Sale.builder()
                .id(1L)
                .listing(listing)
                .date(LocalDate.of(2023,10,9))
                .build();
        sales.add(sale);

        given(saleService.findSalesBySellerId(sellerId)).willReturn(Optional.of(sales));

        ResultActions response = mockMvc.perform(get("/api/sales/get/seller/{sellerId}", sellerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sales)));

        System.out.println(objectMapper.writeValueAsString(sales));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", is(sale.getId().intValue())))
                .andExpect(jsonPath("[0].listing.id", is(listing.getId().intValue())))
                .andExpect(jsonPath("[0].listing.seller.id", is(seller.getId().intValue())));
    }

//    @Test
//    public void givenSaleStatusId_whenUpdateSaleStatus_thenChecksStatusUpdate() throws Exception {
//        Long saleStatusId = 1L;
//        Long saleId = 1L;
//
//        doNothing().when(saleService).updateSaleStatus(saleId, saleStatusId);
//
//        ResultActions response = mockMvc.perform(patch("/api/sales/status/{Id}", saleId)
//                .param("statusId", "1")
//                .contentType(MediaType.APPLICATION_JSON));
//
//        response.andExpect(status().isOk());
//        verify(saleService, times(1)).updateSaleStatus(saleId, saleStatusId);
//    }
}
