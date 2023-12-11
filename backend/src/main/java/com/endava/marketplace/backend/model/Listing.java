package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

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

    @Column()
    private String name;

    @Column(length = 500)
    @Size(max = 500)
    private String detail;

    @Column()
    private Double price;

    @Column()
    private Integer stock;

    @Column()
    private Integer condition;

    @Column()
    private LocalDate date;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL)
    private Set<Question> questions;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.PERSIST)
    private Set<Sale> sales;

    public boolean anyNull() {
        return Stream.of(name, detail, price, stock, condition).anyMatch(Objects::isNull);
    }
}
