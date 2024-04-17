package com.vmoon.carx.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsDto {
    private long id;
    @NotEmpty(message = "cost name must not be empty")
    private String costName;
    private Double cost;
    private LocalDate date;
    private int stock;
    private int quantity;
    private GoodsCategoryDto category;
    private CarBrandDto carBrand;
    private Set<CarModelDto> compatibleModels;
}



