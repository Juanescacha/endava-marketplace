package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.service.ListingService;
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
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ListingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ListingControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ListingService listingService;

    @Test
    public void givenListingInfo_whenSaveListing_thenReturnsCreatedListing() throws Exception{
        Listing listing = Listing.builder()
                .id(1L)
                .name("Listing #1")
                .detail("Example detail")
                .price(10000.0)
                .stock(1)
                .condition(5)
                .date(LocalDate.of(2023,10,12))
                .build();

        given(listingService.saveListing(any(Listing.class))).
                willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/api/listings/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listing)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(listing.getId().intValue())))
                .andExpect(jsonPath("$.name", CoreMatchers.is((listing.getName()))));
    }

    @Test
    public void givenListingId_whenGetListingById_ReturnsListing() throws Exception {
        Long listingId = 1L;

        Listing listing = Listing.builder()
                .id(1L)
                .name("Listing #1")
                .detail("Example detail")
                .price(10000.0)
                .stock(1)
                .condition(5)
                .date(LocalDate.of(2023,10,12))
                .build();

        given(listingService.findListingById(listingId)).willReturn(Optional.of(listing));

        ResultActions response = mockMvc.perform(get("/api/listings/get/{listingId}", listingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listing)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(listing.getId().intValue())))
                .andExpect(jsonPath("$.name", is(listing.getName())));
    }

    @Test
    public void givenListingName_whenGetListingByName_thenReturnListingsThatMatch() throws Exception{
        String listingName = "listing";

        Set<Listing> listings = new HashSet<>();
        Listing listing = Listing.builder()
                .id(1L)
                .name("Listing #1")
                .detail("Example detail")
                .price(10000.0)
                .stock(1)
                .condition(5)
                .date(LocalDate.of(2023,10,12))
                .build();
        listings.add(listing);

        given(listingService.findListingByName(listingName)).willReturn(listings);

        ResultActions response = mockMvc.perform(get("/api/listings/search/quick")
                .param("name", listingName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listings)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", is(listing.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(listing.getName())));
    }

    // Queda pendiente el test de la busqueda con filtros

    @Test
    public void givenListingId_whenDeleteListingById_thenChecksDeletion() throws Exception{
        Long listingId = 1L;

        doNothing().when(listingService).deleteListingById(listingId);

        ResultActions response = mockMvc.perform(delete("/api/listings/delete/{listingId}", listingId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
        verify(listingService, times(1)).deleteListingById(listingId);
    }

    // Queda pendiente el test de la multipart file

    @Test
    public void givenListingId_whenGetListingImages_thenReturnsUrls() throws Exception{
        Long listingId = 1L;
        List<String> urls = new ArrayList<>();
        urls.add("image1UrlExample");
        urls.add("image2UrlExample");
        urls.add("image3UrlExample");

        given(listingService.retrieveListingImages(listingId)).willReturn(urls);

        ResultActions response = mockMvc.perform(get("/api/listings/get/images/{listingId}", listingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(urls)));

        response.andExpect(status().isOk());
        verify(listingService, times(1)).retrieveListingImages(listingId);
    }

    @Test
    public void givenListingId_whenGetListingThumbnail_thenReturnsUrl() throws Exception{
        Long listingId = 1L;
        String thumbnail = "image1ThumbExample";

        given(listingService.retrieveListingThumbnail(listingId)).willReturn(thumbnail);

        ResultActions response = mockMvc.perform(get("/api/listings/get/images/thumb/{listingId}", listingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(thumbnail)));

        response.andExpect(status().isOk());
        verify(listingService, times(1)).retrieveListingThumbnail(listingId);
    }

}
