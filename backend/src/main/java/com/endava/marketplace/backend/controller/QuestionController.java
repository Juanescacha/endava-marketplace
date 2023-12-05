package com.endava.marketplace.backend.controller;

import com.endava.marketplace.backend.dto.*;
import com.endava.marketplace.backend.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/questions")
@Tag(name = "Question", description = "Questions management module")
@SecurityRequirement(name = "Azure AD")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Operation(
            summary = "Creates a new question",
            description = "Creates a new Question. It will be associated to a listing_id and a buyer_id",
            tags = {"Question"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(schema = @Schema(implementation = NewQuestionResponseDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Listing or Endavan with given Id wasn't found", content = { @Content(schema = @Schema()) }),

    })
    @PostMapping()
    public ResponseEntity<NewQuestionResponseDTO> postQuestion(@RequestBody NewQuestionRequestDTO NewQuestionRequestDTO) {
        return ResponseEntity.ok(questionService.saveQuestion(NewQuestionRequestDTO));
    }

    @Operation(
            summary = "Gets all questions of a indicated Listing",
            description = "Fetches all questions of a indicated Listing. If the Listing doesn't have nay related Questions it will return an empty array",
            tags = {"Question"}
    )
    @GetMapping("/listing/{id}")
    public ResponseEntity<Set<QuestionDTO>> getListingQuestions(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.fetchListingQuestions(id));
    }

    @Operation(
            summary = "Answers an existing Question",
            description = "Updates the answer_detail and answer_date of an existing Question",
            tags = {"Question"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(schema = @Schema(implementation = QuestionAnswerDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Question with given Id was not found", content = { @Content(schema = @Schema()) }),

    })
    @PatchMapping("/answer")
    public ResponseEntity<QuestionDTO> answerQuestion(@RequestBody QuestionAnswerDTO questionAnswerDTO) {
        return ResponseEntity.ok(questionService.answerQuestion(questionAnswerDTO));
    }
}
