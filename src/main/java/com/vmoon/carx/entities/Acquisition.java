package com.vmoon.carx.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "acquisition")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Acquisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "total_price")
    private Double totalPrice;
    private LocalDate date;
    private Integer quantity;
}
