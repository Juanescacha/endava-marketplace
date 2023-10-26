package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(columnDefinition = "boolean default true", nullable = false)
    @NotNull
    private Boolean active;

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private Set<Listing> listings;
}
