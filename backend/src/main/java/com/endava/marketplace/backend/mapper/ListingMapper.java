package com.endava.marketplace.backend.mapper;

import com.endava.marketplace.backend.dto.*;
import com.endava.marketplace.backend.model.Listing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Set;

@Mapper(uses = {EndavanMapper.class})
public interface ListingMapper {
    @Mappings({
            @Mapping(source = "category.name", target = "category"),
            @Mapping(source = "status.name", target = "status"),
            @Mapping(source = "date", target = "date", dateFormat = "yyyy-MM-dd")
    })
    ListingDTO toListingDTO(Listing listing);

    @Mappings({
            @Mapping(source = "category.name", target = "category"),
            @Mapping(source = "status.name", target = "status"),
            @Mapping(source = "date", target = "date", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "images", ignore = true),
            @Mapping(target = "thumbnail", ignore = true)
    })
    ListingWithImagesDTO toListingWithImagesDTO(Listing listing);

    ListingQuickSearchDTO toListingQuickSearchDTO(Listing listing);

    Set<ListingQuickSearchDTO> toListingQuickSearchDTOSet(Set<Listing> listings);

    @Mappings({
            @Mapping(source = "category.name", target = "category"),
            @Mapping(source = "status.name", target = "status"),
            @Mapping(source = "date", target = "date", dateFormat = "yyyy-MM-dd")
    })
    ListingWithoutSellerDTO toListingWithoutSellerDTO(Listing listing);

    @Mappings({
            @Mapping(source = "seller_id", target = "seller.id"),
            @Mapping(source = "category_id", target = "category.id"),
            @Mapping(target = "date", ignore = true),
            @Mapping(target = "questions", ignore = true),
            @Mapping(target = "sales", ignore = true),
            @Mapping(target = "status", ignore = true)
    })
    Listing toListing(NewListingRequestDTO newListingRequestDTO);

    @Mappings({
            @Mapping(target = "thumbnail", ignore = true)
    })
    ListingPageDTO toListingPageDTO(Listing listing);
}
