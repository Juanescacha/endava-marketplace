package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rating")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @Column(name = "endavan_id")
    private Long id;

    @Column(columnDefinition = "decimal(2,1)")
    private Double score;

    @Column(columnDefinition = "integer default 0")
    private Integer quantity;

    @OneToOne
    @MapsId
    @JoinColumn(name = "endavan_id")
    private Endavan endavan;
}
