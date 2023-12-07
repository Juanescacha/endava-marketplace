package com.endava.marketplace.backend.model;

import jakarta.persistence.*;
import lombok.*;

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

    @EqualsAndHashCode.Exclude
    @OneToOne
    @MapsId
    @JoinColumn(name = "endavan_id")
    private Endavan endavan;
}
