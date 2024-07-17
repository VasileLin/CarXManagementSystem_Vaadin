package com.vmoon.carx.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String phone;
    @Column(name = "car_number")
    private String carNumber;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id",nullable = false)
    private CarBrand carBrand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id",nullable = false)
    private CarModel carModel;

    private Boolean isDeleted;

}
