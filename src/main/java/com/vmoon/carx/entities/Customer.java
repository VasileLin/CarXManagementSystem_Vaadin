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
    @Column(name = "car_model")
    private String carModel;
    private String email;

}
