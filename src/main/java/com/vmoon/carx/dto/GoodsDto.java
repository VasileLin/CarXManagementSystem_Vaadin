package com.vmoon.carx.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsDto {
    private long id;
    @NotEmpty(message = "Cost name must be not empty!")
    private String costName;
    private Double cost;
    private LocalDate date;
    private int stock;
    private int quantity;
    @NotNull(message = "Good category is required!")
    private GoodsCategoryDto category;
    @NotNull(message = "Car brand is required!")
    private CarBrandDto carBrand;
    @NotNull(message = "Select compatible models!")
    private Set<CarModelDto> compatibleModels;
}



