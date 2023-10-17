package com.endava.marketplace.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenTestPublic_thenReturnsPublicTestMessage() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/test/public")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
    }
    @Test
    public void whenTestPrivate_thenReturnsPrivateTestMessage() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/test/private")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
    }
}
