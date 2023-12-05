package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.dto.NewQuestionRequestDTO;
import com.endava.marketplace.backend.dto.NewQuestionResponseDTO;
import com.endava.marketplace.backend.dto.QuestionAnswerDTO;
import com.endava.marketplace.backend.dto.QuestionDTO;
import com.endava.marketplace.backend.exception.EntityNotFoundException;
import com.endava.marketplace.backend.mapper.QuestionMapper;
import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.model.Question;
import com.endava.marketplace.backend.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    private final ListingService listingService;

    private final EndavanService endavanService;

    private final QuestionMapper questionMapper;

    public QuestionService(QuestionRepository questionRepository, ListingService listingService, EndavanService endavanService, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.listingService = listingService;
        this.endavanService = endavanService;
        this.questionMapper = questionMapper;
    }

    public NewQuestionResponseDTO saveQuestion(NewQuestionRequestDTO newQuestionRequestDTO) {
        Question question = questionMapper.toQuestion(newQuestionRequestDTO);
        Listing listing = listingService.loadListing(newQuestionRequestDTO.getListing_id());
        Endavan buyer = endavanService.loadEndavan((newQuestionRequestDTO.getBuyer_id()));
        question.setListing(listing);
        question.setBuyer(buyer);
        question.setQuestion_date(LocalDate.now());
        return questionMapper.toNewQuestionResponseDTO(questionRepository.save(question));
    }

    public Set<QuestionDTO> fetchListingQuestions(Long id) {
        return questionMapper.toQuestionDTOSet(questionRepository.findAllByListing_Id(id));
    }

    @Transactional
    public QuestionDTO answerQuestion(QuestionAnswerDTO questionAnswerDTO) {
        Optional<Question> foundQuestion = questionRepository.findById(questionAnswerDTO.getId());
        if(foundQuestion.isEmpty()) {
            throw new EntityNotFoundException("Question with ID: " + questionAnswerDTO.getId() + "wasn't found");
        }
        Question question = foundQuestion.get();
        question.setAnswer_detail(questionAnswerDTO.getAnswer_detail());
        question.setAnswer_date(LocalDate.now());
        return questionMapper.toQuestionDTO(questionRepository.save(question));
    }
}
