package com.endava.marketplace.backend.mapper;

import com.endava.marketplace.backend.dto.ListingCategoryDTO;
import com.endava.marketplace.backend.model.ListingCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses = {ListingCategory.class})
public interface ListingCategoryMapper {
    @Mappings({
            @Mapping(target = "active", ignore = true),
            @Mapping(target = "listings", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    ListingCategory toListingCategory(ListingCategoryDTO listingCategoryDTO);
}
