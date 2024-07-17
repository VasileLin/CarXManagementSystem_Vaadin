package com.vmoon.carx.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_model")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String model;
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private CarBrand carBrand;

}
