package com.endava.marketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewQuestionRequestDTO {
    private Long id;
    private Long listing_id;
    private Long buyer_id;
    private String question_detail;
}
