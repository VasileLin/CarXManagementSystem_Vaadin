package com.vmoon.carx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarModelDto that = (CarModelDto) o;
        return id == that.id && year == that.year && Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, year);
    }
}
