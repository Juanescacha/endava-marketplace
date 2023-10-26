package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "listing_status")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @OneToMany(mappedBy = "status", cascade = CascadeType.PERSIST)
    private Set<Listing> listings;
}
