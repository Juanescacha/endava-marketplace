package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "listing")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Endavan seller;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ListingCategory category;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private ListingStatus status;

    private String name;

    private String detail;

    private Double price;

    private Integer stock;

    private Integer condition;

    private String media;

    @OneToMany(mappedBy = "listing")
    private Set<Question> questions;

    @OneToMany(mappedBy = "listing")
    private Set<Sale> sales;
}
