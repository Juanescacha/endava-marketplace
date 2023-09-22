package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "endavan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Endavan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String email;

    private Boolean admin;

    @OneToMany(mappedBy = "seller")
    private Set<Listing> listings;

    @OneToMany(mappedBy = "buyer")
    private Set<Question> questions;

    @OneToMany(mappedBy = "buyer")
    private Set<Sale> sales;
}
