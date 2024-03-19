package com.vmoon.carx.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cost_of_good")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "cost_name")
    private String costName;
    private Double cost;
    private LocalDate date;
    private int stock;
}
