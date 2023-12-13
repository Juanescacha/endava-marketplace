package com.endava.marketplace.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenTestingPublicEndpoint_returnsString() throws Exception{
        // Given
        // When
        ResultActions response = mockMvc.perform(get("/api/test/public"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").value("This is a public test endpoint and its NOT protected by azure ad."));
    }

    @Test
    public void whenTestingPrivateEndpoint_returnsString() throws Exception{
        // Given
        // When
        ResultActions response = mockMvc.perform(get("/api/test/private"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").value("This is a private test endpoint and is protected by azure ad"));
    }
}
