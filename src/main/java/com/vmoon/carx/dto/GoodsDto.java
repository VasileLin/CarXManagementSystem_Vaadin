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
public class GoodsDto {
    private long id;
    private String costName;
    private Double cost;
    private LocalDate date;
    private int stock;
    private int quantity;
}

