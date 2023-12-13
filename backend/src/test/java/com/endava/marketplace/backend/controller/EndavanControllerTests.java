package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.dto.EndavanAdminDTO;
import com.endava.marketplace.backend.dto.EndavanDTO;
import com.endava.marketplace.backend.dto.RatingDTO;
import com.endava.marketplace.backend.service.EndavanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EndavanController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EndavanControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private EndavanService endavanService;

    private RatingDTO ratingDTO;
    @BeforeEach
    void setUp(){
        ratingDTO = RatingDTO.builder()
                .score(10.0)
                .quantity(2)
                .build();


    }

    @Test
    public void givenEndavanInfo_whenSaveEndavan_thenReturnsSavedEndavan() throws Exception{
        // Given
        EndavanDTO endavanDTO = EndavanDTO.builder()
                .id(1L)
                .name("Endavan")
                .email("endavan@endava.com")
                .rating(ratingDTO)
                .build();

        given(endavanService.saveEndavan()).willReturn(endavanDTO);

        // When
        ResultActions response = mockMvc.perform(post("/api/endavans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(endavanDTO)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Endavan"))
                .andExpect(jsonPath("$.email").value("endavan@endava.com"));
    }

    @Test
    public void givenEndavanNameOrEmail_whenFindEndavans_ReturnsEndavans() throws Exception {
        // Given
        EndavanAdminDTO endavanOne = EndavanAdminDTO.builder()
                .id(1L)
                .name("EndavanOne")
                .email("endavanone@endava.com")
                .admin(true)
                .build();

        EndavanAdminDTO endavanTwo = EndavanAdminDTO.builder()
                .id(2L)
                .name("EndavanTwo")
                .email("endavantwo@endava.com")
                .admin(false)
                .build();

        Page<EndavanAdminDTO> results = new PageImpl<>(List.of(endavanOne, endavanTwo));

        given(endavanService.findEndavans(null, null, null)).willReturn(results);

        // When
        ResultActions response = mockMvc.perform(get("/api/endavans/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(results.getContent())));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2));
    }

    @Test
    public void givenEndavanId_whenGetEndavanById_ReturnsEndavanDTO() throws Exception {
        // Given
        Long endavanId = 1L;

        EndavanDTO endavanDTO = EndavanDTO.builder()
                .id(1L)
                .name("Endavan")
                .email("endavan@endava.com")
                .rating(ratingDTO)
                .build();

        given(endavanService.findEndavanById(endavanId)).willReturn(endavanDTO);

        // When
        ResultActions response = mockMvc.perform(get("/api/endavans/{endavanId}", endavanId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(endavanDTO)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Endavan"))
                .andExpect(jsonPath("$.email").value("endavan@endava.com"));
    }


    @Test
    public void givenEndavanId_whenDeleteEndavanById_thenChecksDeletion() throws Exception {
        // Given
        Long endavanId = 1L;

        doNothing().when(endavanService).deleteEndavanById(endavanId);

        // When
        ResultActions response = mockMvc.perform(delete("/api/endavans/{endavanId}", endavanId)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isOk());
        verify(endavanService, times(1)).deleteEndavanById(endavanId);
    }

    @Test
    public void givenEndavanIdAndAdminFlag_whenUpdateAdminRole_ReturnsUpdatedEndavanDTO() throws Exception {
        // Given
        Long endavanId = 1L;
        boolean adminFlag = true;

        EndavanAdminDTO endavanDTO = EndavanAdminDTO.builder()
                .id(1L)
                .name("Endavan")
                .email("endavan@endava.com")
                .admin(adminFlag)
                .build();

        given(endavanService.updateAdminRole(endavanId, adminFlag)).willReturn(endavanDTO);

        // When
        ResultActions response = mockMvc.perform(patch("/api/endavans/update-admin")
                .param("endavanId", String.valueOf(endavanId))
                .param("admin", String.valueOf(adminFlag))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(endavanDTO)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Endavan"))
                .andExpect(jsonPath("$.email").value("endavan@endava.com"))
                .andExpect(jsonPath("$.admin").value(adminFlag));
    }

    @Test
    public void givenLoginCredentials_whenCheckAdminRole_thenReturnsBoolean() throws Exception{
        // Given
        boolean adminFlag = true;

        given(endavanService.checkAdminRole()).willReturn(adminFlag);

        // When
        ResultActions response = mockMvc.perform(get("/api/endavans/check-admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminFlag)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(adminFlag));
    }

}
