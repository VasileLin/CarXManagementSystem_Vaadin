package com.vmoon.carx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarModelDto {

    private int id;
    private String model;
    private int year;
    private CarBrandDto carBrand;

    @Override
    public String toString() {
        return model;
    }
}
