package com.endava.marketplace.backend.mapper;

import com.endava.marketplace.backend.dto.RatingDTO;
import com.endava.marketplace.backend.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface RatingMapper {
    RatingDTO toRatingDTO(Rating rating);
}
