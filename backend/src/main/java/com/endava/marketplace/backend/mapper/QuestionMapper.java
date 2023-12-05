package com.endava.marketplace.backend.mapper;

import com.endava.marketplace.backend.dto.NewQuestionRequestDTO;
import com.endava.marketplace.backend.dto.NewQuestionResponseDTO;
import com.endava.marketplace.backend.dto.QuestionDTO;
import com.endava.marketplace.backend.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Set;

@Mapper(uses = {EndavanMapper.class, ListingMapper.class})
public interface QuestionMapper {
    @Mappings({
            @Mapping(source = "listing_id", target = "listing.id"),
            @Mapping(source = "buyer_id", target = "buyer.id"),
            @Mapping(target = "answer_detail", ignore = true),
            @Mapping(target = "answer_date", ignore = true),
            @Mapping(target = "question_date", ignore = true)
    })
    Question toQuestion(NewQuestionRequestDTO newQuestionRequestDTO);

    @Mappings({})
    NewQuestionResponseDTO toNewQuestionResponseDTO(Question question);

    @Mappings({})
    QuestionDTO toQuestionDTO(Question question);

    Set<QuestionDTO> toQuestionDTOSet(Set<Question> questions);
}
