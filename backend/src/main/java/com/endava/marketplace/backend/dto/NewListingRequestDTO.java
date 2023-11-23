package com.endava.marketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewListingRequestDTO {
    private Long id;

    private Long seller_id;

    private Long category_id;

    private String name;

    private String detail;

    private Double price;

    private Integer stock;

    private Integer condition;
}
