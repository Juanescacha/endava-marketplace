package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Endavan seller;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ListingCategory category;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private ListingStatus status;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(length = 500, nullable = false)
    @Size(max = 500)
    @NotNull
    private String detail;

    @Column(nullable = false)
    @NotNull
    private Double price;

    @Column(nullable = false)
    @NotNull
    private Integer stock;

    @Column(nullable = false)
    @NotNull
    private Integer condition;

    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL)
    private Set<Question> questions;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.PERSIST)
    private Set<Sale> sales;
}
