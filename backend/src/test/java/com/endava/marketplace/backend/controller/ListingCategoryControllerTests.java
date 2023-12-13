package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.dto.ActiveListingCategoryDTO;
import com.endava.marketplace.backend.dto.ListingCategoryDTO;
import com.endava.marketplace.backend.service.ListingCategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ListingCategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ListingCategoryControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ListingCategoryService listingCategoryService;

    @Test
    public void givenListingCategoryInfo_whenSaveListingCategory_thenReturnsSavedCategory() throws Exception{
        // Given
        String categoryName = "Sports";
        ListingCategoryDTO listingCategoryDTO = ListingCategoryDTO.builder()
                .id(1L)
                .name(categoryName)
                .active(true)
                .build();

        given(listingCategoryService.saveListingCategory(categoryName)).willReturn(listingCategoryDTO);

        // When
        ResultActions response = mockMvc.perform(post("/api/categories")
                .param("name", categoryName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listingCategoryDTO)));

        // Then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(categoryName));
    }

    @Test
    public void whenGetAllListingCategories_thenReturnsListOfCategories() throws Exception{
        // Given
        ListingCategoryDTO sportsCategoryDTO = ListingCategoryDTO.builder()
                .id(1L)
                .name("Sports")
                .active(true)
                .build();
        ListingCategoryDTO techCategoryDTO = ListingCategoryDTO.builder()
                .id(2L)
                .name("Tech")
                .active(false)
                .build();

        List<ListingCategoryDTO> allCategories = List.of(sportsCategoryDTO, techCategoryDTO);

        given(listingCategoryService.fetchAllListingCategories()).willReturn(allCategories);

        // When
        ResultActions response = mockMvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(allCategories)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("[0].name").value("Sports"))
                .andExpect(jsonPath("[1].name").value("Tech"));
    }

    @Test
    public void whenGetAllActiveListingCategories_thenReturnsListOfActiveCategories() throws Exception{
        // Given
        ActiveListingCategoryDTO sportsCategoryDTO = ActiveListingCategoryDTO.builder()
                .id(1L)
                .name("Sports")
                .build();
        ActiveListingCategoryDTO techCategoryDTO = ActiveListingCategoryDTO.builder()
                .id(2L)
                .name("Tech")
                .build();

        List<ActiveListingCategoryDTO> activeCategories = List.of(sportsCategoryDTO, techCategoryDTO);

        given(listingCategoryService.fetchAllActiveListingCategories()).willReturn(activeCategories);

        // When
        ResultActions response = mockMvc.perform(get("/api/categories/active")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(activeCategories)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("[0].name").value("Sports"))
                .andExpect(jsonPath("[1].name").value("Tech"));
    }

    @Test
    public void givenCategoryId_whenEnableListingCategory_thenReturnsEnabledCategory() throws Exception{
        // Given
        Long categoryId = 1L;
        boolean activeFlag = true;

        ListingCategoryDTO sportsCategoryDTO = ListingCategoryDTO.builder()
                .id(categoryId)
                .name("Sports")
                .active(true)
                .build();

        given(listingCategoryService.updateListingCategoryActiveStatus(categoryId, activeFlag)).willReturn(sportsCategoryDTO);

        // When
        ResultActions response = mockMvc.perform(patch("/api/categories/{categoryId}/enable", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sportsCategoryDTO)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.active").value(activeFlag));
    }

    @Test
    public void givenCategoryId_whenDisableListingCategory_thenReturnsDisabledCategory() throws Exception{
        // Given
        Long categoryId = 1L;
        boolean activeFlag = false;

        ListingCategoryDTO sportsCategoryDTO = ListingCategoryDTO.builder()
                .id(categoryId)
                .name("Sports")
                .active(activeFlag)
                .build();

        given(listingCategoryService.updateListingCategoryActiveStatus(categoryId, activeFlag)).willReturn(sportsCategoryDTO);

        // When
        ResultActions response = mockMvc.perform(patch("/api/categories/{categoryId}/disable", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sportsCategoryDTO)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.active").value(activeFlag));
    }

    @Test
    public void givenCategoryId_whenPatchListingCategoryName_thenReturnsRenamedCategory() throws Exception{
        // Given
        Long categoryId = 1L;
        String newName = "Sports equipment";

        ListingCategoryDTO sportsCategoryDTO = ListingCategoryDTO.builder()
                .id(categoryId)
                .name(newName)
                .active(true)
                .build();

        given(listingCategoryService.updateListingCategoryName(categoryId, newName)).willReturn(sportsCategoryDTO);

        // When
        ResultActions response = mockMvc.perform(patch("/api/categories/{categoryId}/rename", categoryId)
                .param("name", newName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sportsCategoryDTO)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value(newName));
    }
}
