package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "sale")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Endavan buyer;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private SaleStatus status;

    @Column()
    private LocalDate date = LocalDate.now();
}
