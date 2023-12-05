package com.endava.marketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewQuestionResponseDTO {
    private Long id;
    private ListingDTO listing;
    private EndavanDTO buyer;
    private String question_detail;
    private LocalDate question_date;
}
