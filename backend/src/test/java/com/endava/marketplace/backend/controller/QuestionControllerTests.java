package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.dto.*;
import com.endava.marketplace.backend.model.*;
import com.endava.marketplace.backend.service.QuestionService;
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
import java.util.Set;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = QuestionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuestionControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private QuestionService questionService;

    private Endavan buyer;
    private EndavanDTO buyerDTO;
    private Listing listing;
    private ListingDTO listingDTO;

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
    }

    @Test
    public void givenNewQuestionRequest_whenSaveQuestion_thenReturnsNewQuestionResponse() throws Exception {
        NewQuestionRequestDTO newQuestionRequest = NewQuestionRequestDTO.builder()
                .listing_id(listing.getId())
                .buyer_id(buyer.getId())
                .question_detail("Detail of Question #1")
                .build();

        NewQuestionResponseDTO newQuestionResponse = NewQuestionResponseDTO.builder()
                .id(1L)
                .listing(listingDTO)
                .buyer(buyerDTO)
                .question_detail(newQuestionRequest.getQuestion_detail())
                .question_date(LocalDate.now())
                .build();

        given(questionService.saveQuestion(newQuestionRequest)).willReturn(newQuestionResponse);

        ResultActions response = mockMvc.perform(post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newQuestionRequest)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newQuestionResponse.getId()));

        verify(questionService, times(1)).saveQuestion(newQuestionRequest);
    }

    @Test
    public void givenListingId_whenFetchingListingQuestions_thenReturnQuestionsThatMatch() throws Exception {
        QuestionDTO question1 = QuestionDTO.builder()
                .id(1L)
                .buyer(buyerDTO)
                .question_detail("Detail of Question #1")
                .question_date(LocalDate.now())
                .build();

        QuestionDTO question2 = QuestionDTO.builder()
                .id(2L)
                .buyer(buyerDTO)
                .question_detail("Detail of Question #2")
                .question_date(LocalDate.now())
                .build();

        Set<QuestionDTO> questionsDTOSet = Set.of(question1, question2);

        given(questionService.fetchListingQuestions(listing.getId())).willReturn(questionsDTOSet);

        ResultActions response = mockMvc.perform(get("/api/questions/listing/{id}", listing.getId())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(questionService, times(1)).fetchListingQuestions(listing.getId());
    }

    @Test
    public void givenQuestionAnswer_whenAnsweringQuestion_thenReturnAnsweredQuestion() throws Exception {
        QuestionAnswerDTO questionAnswer = QuestionAnswerDTO.builder()
                .id(1L)
                .answer_detail("Answer of Question #1")
                .build();

        QuestionDTO answeredQuestionDTO = QuestionDTO.builder()
                .id(questionAnswer.getId())
                .buyer(buyerDTO)
                .question_detail("Detail of Question #1")
                .answer_detail(questionAnswer.getAnswer_detail())
                .question_date(LocalDate.now())
                .answer_date(LocalDate.now())
                .build();

        given(questionService.answerQuestion(questionAnswer)).willReturn(answeredQuestionDTO);

        ResultActions response = mockMvc.perform(patch("/api/questions/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionAnswer)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.answer_detail").value(questionAnswer.getAnswer_detail()))
                .andExpect(jsonPath("$.answer_date").exists());
    }
}
