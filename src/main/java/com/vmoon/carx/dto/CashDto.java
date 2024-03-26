package com.vmoon.carx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashDto {

    private int id;
    private String transactionNo;
    private Double price;
    private LocalDate date;
    private String status;
    private String details;
    private String receiptPath;

    private Set<ServiceDto> services;
    private Set<GoodsDto> goods;
    private CustomerDto customer;
}
