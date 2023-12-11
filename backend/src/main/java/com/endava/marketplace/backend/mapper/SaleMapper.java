package com.endava.marketplace.backend.mapper;

import com.endava.marketplace.backend.dto.*;
import com.endava.marketplace.backend.model.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Set;

@Mapper(uses = {EndavanMapper.class, ListingMapper.class})
public interface SaleMapper {
    @Mappings({
            @Mapping(source = "status.name", target = "status")
    })
    SaleDTO toSaleDTO(Sale sale);

    @Mappings({
            @Mapping(source = "buyer_id", target = "buyer.id"),
            @Mapping(source = "listing_id", target = "listing.id"),
            @Mapping(target = "date", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "rating", ignore = true)
    })
    Sale toSale(NewSaleRequestDTO newSaleRequestDTO);

    @Mappings({
            @Mapping(source = "status.name", target = "status"),
            @Mapping(source = "listing.seller.name", target = "seller_name"),
            @Mapping(source = "buyer.name", target = "buyer_name"),
            @Mapping(source = "listing.id", target = "listing_id"),
            @Mapping(source = "listing.name", target = "listing_name"),
            @Mapping(source = "listing.price", target = "listing_price"),
            @Mapping(target = "listing_thumbnail", ignore = true)
    })
    ListedSaleDTO toListedSaleDTO(Sale sale);

    Set<ListedSaleDTO> toListedSaleDTOSet(Set<Sale> sales);
}
