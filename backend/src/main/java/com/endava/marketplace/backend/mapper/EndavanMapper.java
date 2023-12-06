package com.endava.marketplace.backend.mapper;

import com.endava.marketplace.backend.dto.EndavanAdminDTO;
import com.endava.marketplace.backend.dto.EndavanDTO;
import com.endava.marketplace.backend.model.Endavan;
import org.mapstruct.Mapper;


@Mapper(uses = {RatingMapper.class})
public interface EndavanMapper {
    EndavanDTO toEndavanDTO(Endavan endavan);

    EndavanAdminDTO toEndavanAdminDTO(Endavan endavan);
}
