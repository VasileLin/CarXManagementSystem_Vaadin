package com.vmoon.carx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

    private List<ServiceDto> services;
    private List<GoodsDto> goods;
    private CustomerDto customer;
}
