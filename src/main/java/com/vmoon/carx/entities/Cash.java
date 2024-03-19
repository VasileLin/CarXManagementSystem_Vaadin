package com.vmoon.carx.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cash")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "transaction_no")
    private String transactionNo;
    private Double price;
    private LocalDate date;
    private String status;
    @Column(length = 1024)
    private String details;

    @ManyToMany
    @JoinTable(
            name = "cash_service",
            joinColumns = {@JoinColumn(name = "cash_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "service_id", referencedColumnName = "id")}
    )
    private List<Service> services;

    @ManyToMany
    @JoinTable(
            name = "cash_goods",
            joinColumns = {@JoinColumn(name = "cash_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "good_id", referencedColumnName = "id")}
    )
    private List<Goods> goods;

    @OneToOne
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;


}
