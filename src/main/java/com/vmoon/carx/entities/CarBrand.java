package com.vmoon.carx.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "car_brand")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String brand;

    @OneToMany(mappedBy = "carBrand")
    private List<Goods> goodsList;
}
