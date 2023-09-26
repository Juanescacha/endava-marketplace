package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Long id;

    @Column()
    @NotNull
    private String name;

    @Column(unique = true)
    @NotNull
    private String email;

    @Column(columnDefinition = "boolean default false")
    @NotNull
    private Boolean admin;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<Listing> listings;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private Set<Question> questions;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.PERSIST)
    private Set<Sale> sales;
}
