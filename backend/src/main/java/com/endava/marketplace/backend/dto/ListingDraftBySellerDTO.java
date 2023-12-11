package com.endava.marketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingDraftBySellerDTO {
    private Long id;

    private String category;

    private String status;

    private String name;

    private String detail;
}
