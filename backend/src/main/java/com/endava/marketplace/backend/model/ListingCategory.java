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

    @Column()
    @NotNull
    private String category;

    @Column(columnDefinition = "boolean default true")
    @NotNull
    private Boolean active;

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private Set<Listing> listings;
}
