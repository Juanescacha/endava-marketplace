package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "listing_category")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Boolean active;

    @OneToMany(mappedBy = "category")
    private Set<Listing> listings;
}
