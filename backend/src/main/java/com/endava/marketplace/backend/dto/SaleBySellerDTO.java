package com.endava.marketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleBySellerDTO {
    private Long id;

    private EndavanDTO buyer;

    private ListingWithoutSellerDTO listing;

    private String status;

    private LocalDate date;
}
