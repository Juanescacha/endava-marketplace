package com.endava.marketplace.backend.mapper;

import com.endava.marketplace.backend.dto.NewSaleRequestDTO;
import com.endava.marketplace.backend.dto.SaleByBuyerDTO;
import com.endava.marketplace.backend.dto.SaleBySellerDTO;
import com.endava.marketplace.backend.dto.SaleDTO;
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
            @Mapping(source = "status.name", target = "status")
    })
    SaleByBuyerDTO toBuyerDTO(Sale sale);

    Set<SaleByBuyerDTO> toBuyerDTOSet(Set<Sale> sales);

    @Mappings({
            @Mapping(source = "buyer", target = "buyer"),
            @Mapping(source = "status.name", target = "status")
    })
    SaleBySellerDTO toSellerDTO(Sale sale);

    Set<SaleBySellerDTO> toSellerDTOSet(Set<Sale> sales);

    @Mappings({
            @Mapping(source = "buyer_id", target = "buyer.id"),
            @Mapping(source = "listing_id", target = "listing.id"),
            @Mapping(target = "date", ignore = true),
            @Mapping(target = "status", ignore = true)
    })
    Sale toSale(NewSaleRequestDTO newSaleRequestDTO);
}
