package com.vmoon.carx.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.Set;

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
    private String name;
    private Double cost;
    private LocalDate date;
    private int stock;
    @Column(name = "car_model")
    private String carModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private GoodsCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id",nullable = false)
    private CarBrand carBrand;

    @BatchSize(size = 30)
    @ManyToMany
    @JoinTable(
            name = "model_goods",
            joinColumns = {@JoinColumn(name = "good_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "model_id", referencedColumnName = "id")}
    )
    private Set<CarModel> compatibleModels;
}
