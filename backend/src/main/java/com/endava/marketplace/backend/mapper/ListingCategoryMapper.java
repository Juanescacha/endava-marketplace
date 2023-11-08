package com.endava.marketplace.backend.mapper;

import com.endava.marketplace.backend.dto.ListingCategoryDTO;
import com.endava.marketplace.backend.model.ListingCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.Set;

@Mapper(uses = {ListingCategory.class})
public interface ListingCategoryMapper {

    @Mappings({})
    ListingCategoryDTO toListingCategoryDTO(ListingCategory listingCategory);

    Set<ListingCategoryDTO> toListingCategoryDTOSet(Set<ListingCategory> listingCategories);
}
