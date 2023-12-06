package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @Column(nullable = false)
    @NotNull(message = "Name can't be null")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(unique = true, nullable = false)
    @NotNull(message = "Email can't be null")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Column(columnDefinition = "boolean default false", nullable = false)
    @NotNull(message = "Admin flag can't be null")
    private Boolean admin;

    @OneToOne(mappedBy = "endavan", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Rating rating;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<Listing> listings;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private Set<Question> questions;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.PERSIST)
    private Set<Sale> sales;
}
