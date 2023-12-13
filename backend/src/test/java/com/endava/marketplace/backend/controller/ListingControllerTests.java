package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.dto.*;
import com.endava.marketplace.backend.service.ListingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ListingController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ListingControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ListingService listingService;

    private EndavanDTO endavanDTO;

    @BeforeEach
    void setUp(){
        RatingDTO ratingDTO = RatingDTO.builder()
                .quantity(2)
                .score(10.0)
                .build();

        endavanDTO = EndavanDTO.builder()
                .id(1L)
                .name("Endavan")
                .email("endavan@endava.com")
                .rating(ratingDTO)
                .build();
    }

    @Test
    public void givenListingId_whenGetListingById_ReturnsListingWithImagesDTO() throws Exception {
        // Given
        Long listingId = 1L;

        ListingWithImagesDTO listingDTO = ListingWithImagesDTO.builder()
                .id(1L)
                .seller(endavanDTO)
                .category("Sports")
                .status("Available")
                .name("listing")
                .detail("listing detail")
                .price(500.0)
                .stock(1)
                .condition(10)
                .images(List.of("img1", "img2")).build();

        given(listingService.findListingById(listingId)).willReturn(listingDTO);

        // When
        ResultActions response = mockMvc.perform(get("/api/listings/{listingId}", listingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listingDTO)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(listingDTO.getName()))
                .andExpect(jsonPath("$.detail").value(listingDTO.getDetail()));
    }

    @Test
    public void givenSellerId_whenListingDraftsBySellerId_ReturnsSetOfListingDraftBySellerDTO() throws Exception {
        // Given
        Long sellerId = 1L;

        ListingDraftBySellerDTO listingDTO = ListingDraftBySellerDTO.builder()
                .id(1L)
                .category("Sports")
                .status("Available")
                .name("listingName")
                .detail("listingDetail")
                .build();

        Set<ListingDraftBySellerDTO> data = new HashSet<>(List.of(listingDTO));

        given(listingService.findListingDraftsBySellerId(sellerId)).willReturn(data);

        // When
        ResultActions response = mockMvc.perform(get("/api/listings/seller/{sellerId}/drafts", sellerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
    @Test
    public void givenListingName_whenGetListingByName_ReturnsListOfListingQuickSearchDTO() throws Exception {
        // Given
        String listingName = "listing";

        ListingQuickSearchDTO listingDTO = ListingQuickSearchDTO.builder()
                .id(1L)
                .name("listingName")
                .build();

        Set<ListingQuickSearchDTO> data = new HashSet<>(List.of(listingDTO));

        given(listingService.findListingByName(listingName)).willReturn(data);

        // When
        ResultActions response = mockMvc.perform(get("/api/listings/suggestions")
                .param("name", listingName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
    @Test
    public void givenListingNameOrCategory_whenFindListings_ReturnsListings() throws Exception {
        // Given
        ListingPageDTO listingOne = ListingPageDTO.builder()
                .id(1L)
                .name("listingOne")
                .price(500.0)
                .thumbnail("thumb1")
                .build();

        ListingPageDTO listingTwo = ListingPageDTO.builder()
                .id(2L)
                .name("listingTwo")
                .price(900.0)
                .thumbnail("thumb2")
                .build();

        Page<ListingPageDTO> results = new PageImpl<>(List.of(listingOne, listingTwo));

        given(listingService.findListings(null, null, null)).willReturn(results);

        // When
        ResultActions response = mockMvc.perform(get("/api/listings/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(results.getContent())));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2));
    }
    @Test
    public void givenListingId_whenDeleteListingById_thenChecksDeletion() throws Exception {
        // Given
        Long listingId = 1L;

        doNothing().when(listingService).deleteListingById(listingId);

        // When
        ResultActions response = mockMvc.perform(delete("/api/listings/{listingId}", listingId)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isOk());
        verify(listingService, times(1)).deleteListingById(listingId);
    }
    @Test
    public void givenListingId_whenGetListingImages_thenReturnsListingListOfImageURL() throws Exception{
        // Given
        Long listingId = 1L;
        List<String> images = List.of("url1", "url2", "url3");

        given(listingService.retrieveListingImages(listingId)).willReturn(images);

        // When
        ResultActions response = mockMvc.perform(get("/api/listings/images/{listingId}", listingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(images)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3));
    }

    @Test
    public void givenListingId_whenGetListingThumbnail_thenReturnsListingThumbnailURL() throws Exception{
        // Given
        Long listingId = 1L;
        String thumbUrl = "thumb.url";

        given(listingService.retrieveListingThumbnail(listingId)).willReturn(thumbUrl);

        // When
        ResultActions response = mockMvc.perform(get("/api/listings/thumb/{listingId}", listingId)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(thumbUrl));
    }
}
