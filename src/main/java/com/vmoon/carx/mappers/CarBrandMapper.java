package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CarBrandDto;
import com.vmoon.carx.entities.CarBrand;
import org.springframework.stereotype.Component;

@Component
public class CarBrandMapper {

    public static CarBrandDto toCarBrandDtoWithModels(CarBrand carBrand) {
        CarBrandDto carBrandDto = CarBrandDto.builder()
                .id(carBrand.getId())
                .brand(carBrand.getBrand())
                .build();

        if (carBrand.getCarModels() != null) {
            carBrandDto.setCarModels(carBrand.getCarModels()
                    .stream()
                    .map(CarModelMapper::toCarModelDto)
                    .toList());
        }

        return carBrandDto;
    }

    public static CarBrandDto toCarBrandDto(CarBrand carBrand) {
        return CarBrandDto.builder()
                .id(carBrand.getId())
                .brand(carBrand.getBrand())
                .build();
    }

    public static CarBrand toCarBrand(CarBrandDto carBrandDto) {
        return CarBrand.builder()
                .id(carBrandDto.getId())
                .brand(carBrandDto.getBrand())
                .build();
    }
}
