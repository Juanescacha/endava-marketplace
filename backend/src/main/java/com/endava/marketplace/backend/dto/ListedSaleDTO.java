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
public class ListedSaleDTO {
    private Long id;
    private Integer quantity;
    private LocalDate date;
    private String status;
    private String seller_name;
    private String buyer_name;
    private Long listing_id;
    private String listing_name;
    private Double listing_price;
    private String listing_thumbnail;
}
