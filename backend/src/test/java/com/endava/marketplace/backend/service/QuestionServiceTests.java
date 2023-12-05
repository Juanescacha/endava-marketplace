package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.dto.*;
import com.endava.marketplace.backend.mapper.QuestionMapper;
import com.endava.marketplace.backend.model.*;
import com.endava.marketplace.backend.repository.QuestionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTests {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ListingService listingService;

    @Mock
    private EndavanService endavanService;

    @Mock
    private QuestionMapper questionMapper = Mappers.getMapper(QuestionMapper.class);

    private QuestionService questionService;

    private Endavan buyer;
    private EndavanDTO buyerDTO;
    private Listing listing;
    private ListingDTO listingDTO;

    @BeforeEach
    void setUp() {
        this.questionService = new QuestionService(questionRepository, listingService, endavanService, questionMapper);

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
    public void givenNewQuestionRequest_whenSaveQuestion_thenReturnsNewQuestionResponse() {
        NewQuestionRequestDTO newQuestionRequest = NewQuestionRequestDTO.builder()
                .listing_id(listing.getId())
                .buyer_id(buyer.getId())
                .question_detail("Detail of Question #1")
                .build();

        Question question1 = Question.builder()
                .question_detail(newQuestionRequest.getQuestion_detail())
                .build();

        Question question2 = Question.builder()
                .listing(listing)
                .buyer(buyer)
                .question_detail(question1.getQuestion_detail())
                .question_date(LocalDate.now())
                .build();

        Question question3 = Question.builder()
                .id(1L)
                .listing(question2.getListing())
                .buyer(question2.getBuyer())
                .question_detail(question2.getQuestion_detail())
                .question_date(question2.getQuestion_date())
                .build();

        NewQuestionResponseDTO newQuestionResponse = NewQuestionResponseDTO.builder()
                .id(question3.getId())
                .listing(listingDTO)
                .buyer(buyerDTO)
                .question_detail(question3.getQuestion_detail())
                .question_date(question3.getQuestion_date())
                .build();

        when(questionMapper.toQuestion(newQuestionRequest)).thenReturn(question1);
        when(listingService.loadListing(listing.getId())).thenReturn(listing);
        when(endavanService.loadEndavan(buyer.getId())).thenReturn(buyer);
        when(questionRepository.save(question2)).thenReturn(question3);
        when(questionMapper.toNewQuestionResponseDTO(question3)).thenReturn(newQuestionResponse);

        NewQuestionResponseDTO testQuestion = questionService.saveQuestion(newQuestionRequest);

        Assertions.assertThat(testQuestion).isNotNull();
        Assertions.assertThat(testQuestion.getId()).isGreaterThan(0);
        verify(questionMapper, times(1)).toQuestion(newQuestionRequest);
        verify(listingService, times(1)).loadListing(listing.getId());
        verify(endavanService, times(1)).loadEndavan(buyer.getId());
        verify(questionRepository, times(1)).save(question2);
        verify(questionMapper, times(1)).toNewQuestionResponseDTO(question3);
    }

    @Test
    public void givenListingId_whenFetchingListingQuestions_thenReturnQuestionsThatMatch() {
        Question question1 = Question.builder()
                .id(1L)
                .listing(listing)
                .buyer(buyer)
                .question_detail("Detail of Question #1")
                .question_date(LocalDate.now())
                .build();

        Question question2 = Question.builder()
                .id(2L)
                .listing(listing)
                .buyer(buyer)
                .question_detail("Detail of Question #2")
                .question_date(LocalDate.now())
                .build();

        Set<Question> questionSet = Set.of(question1, question2);

        QuestionDTO question1DTO = QuestionDTO.builder()
                .id(question1.getId())
                .buyer(buyerDTO)
                .question_detail(question1.getQuestion_detail())
                .question_date(question1.getQuestion_date())
                .build();

        QuestionDTO question2DTO = QuestionDTO.builder()
                .id(question2.getId())
                .buyer(buyerDTO)
                .question_detail(question2.getQuestion_detail())
                .question_date(question2.getQuestion_date())
                .build();

        Set<QuestionDTO> questionsDTOSet = Set.of(question1DTO, question2DTO);

        when(questionRepository.findAllByListing_Id(listing.getId())).thenReturn(questionSet);
        when(questionMapper.toQuestionDTOSet(questionSet)).thenReturn(questionsDTOSet);
        Set<QuestionDTO> listingQuestions = questionService.fetchListingQuestions(listing.getId());

        Assertions.assertThat(listingQuestions).doesNotContainNull();
        verify(questionRepository, times(1)).findAllByListing_Id(listing.getId());
        verify(questionMapper, times(1)).toQuestionDTOSet(questionSet);
    }

    @Test
    public void givenQuestionAnswer_whenAnsweringQuestion_thenReturnAnsweredQuestion() {
        QuestionAnswerDTO questionAnswer = QuestionAnswerDTO.builder()
                .id(1L)
                .answer_detail("Answer of Question #1")
                .build();

        Question question = Question.builder()
                .id(questionAnswer.getId())
                .listing(listing)
                .buyer(buyer)
                .question_detail("Detail of Question #1")
                .question_date(LocalDate.now())
                .build();

        Question answeredQuestion = Question.builder()
                .id(question.getId())
                .listing(question.getListing())
                .buyer(question.getBuyer())
                .question_detail(question.getQuestion_detail())
                .answer_detail(questionAnswer.getAnswer_detail())
                .question_date(question.getQuestion_date())
                .answer_date(LocalDate.now())
                .build();

        QuestionDTO answeredQuestionDTO = QuestionDTO.builder()
                .id(answeredQuestion.getId())
                .buyer(buyerDTO)
                .question_detail(answeredQuestion.getQuestion_detail())
                .answer_detail(answeredQuestion.getAnswer_detail())
                .question_date(answeredQuestion.getQuestion_date())
                .answer_date(answeredQuestion.getAnswer_date())
                .build();

        when(questionRepository.findById(questionAnswer.getId())).thenReturn(Optional.of(question));
        when(questionRepository.save(answeredQuestion)).thenReturn(answeredQuestion);
        when(questionMapper.toQuestionDTO(answeredQuestion)).thenReturn(answeredQuestionDTO);
        QuestionDTO questionDTO = questionService.answerQuestion(questionAnswer);

        Assertions.assertThat(questionDTO.getAnswer_detail()).isNotNull();
        Assertions.assertThat(questionDTO.getAnswer_date()).isNotNull();

        verify(questionRepository, times(1)).findById(questionAnswer.getId());
        verify(questionMapper, times(1)).toQuestionDTO(answeredQuestion);
    }
}
