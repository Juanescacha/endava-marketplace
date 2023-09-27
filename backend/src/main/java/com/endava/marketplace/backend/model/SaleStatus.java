package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "sale_status")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    @NotNull
    private String name;

    @OneToMany(mappedBy = "status", cascade = CascadeType.PERSIST)
    private Set<Sale> sales;
}
