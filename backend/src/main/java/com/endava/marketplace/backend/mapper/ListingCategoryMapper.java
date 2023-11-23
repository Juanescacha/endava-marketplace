package com.endava.marketplace.backend.mapper;

import com.endava.marketplace.backend.dto.ListingCategoryDTO;
import com.endava.marketplace.backend.dto.ActiveListingCategoryDTO;
import com.endava.marketplace.backend.model.ListingCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(uses = {ListingCategory.class})
public interface ListingCategoryMapper {

    @Mappings({})
    ListingCategoryDTO toListingCategoryDTO(ListingCategory listingCategory);

    List<ListingCategoryDTO> toListingCategoryDTOList(List<ListingCategory> listingCategories);

    @Mappings({})
    ActiveListingCategoryDTO toActiveListingCategoryDTO(ListingCategory listingCategory);

    List<ActiveListingCategoryDTO> toActiveListingCategoryDTOList(List<ListingCategory> listingCategories);
}

