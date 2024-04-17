package com.vmoon.carx.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarBrandDto {
    private int id;
    @NotNull(message = "Brand name must not be empty!")
    private String brand;
    private List<CarModelDto> carModels;
}
