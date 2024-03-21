package com.vmoon.carx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashGridDto {
    private int id;
    private String transactionNo;
    private Double price;
    private LocalDate date;
    private String status;
    private String details;
}
