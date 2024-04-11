package com.vmoon.carx.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "good_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Goods> goods;
}
