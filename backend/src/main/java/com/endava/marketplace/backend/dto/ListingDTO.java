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
public class ListingDTO {
    private Long id;
    private EndavanDTO seller;
    private String category;
    private String status;
    private String name;
    private String detail;
    private Double price;
    private Integer stock;
    private Integer condition;
    private LocalDate date;
}
