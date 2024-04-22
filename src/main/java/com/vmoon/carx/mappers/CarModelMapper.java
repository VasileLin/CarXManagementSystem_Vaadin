package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CarModelDto;
import com.vmoon.carx.entities.CarModel;
import org.springframework.stereotype.Component;

@Component
public class CarModelMapper {

    public static CarModelDto toCarModelDto(CarModel carModel) {
        CarModelDto carModelDto = CarModelDto.builder()
                .id(carModel.getId())
                .model(carModel.getModel())
                .year(carModel.getYear())
                .build();

        if (carModel.getCarBrand() != null) {
            carModelDto.setCarBrand(CarBrandMapper.toCarBrandDto(carModel.getCarBrand()));
        }

        return carModelDto;
    }

    public static CarModel toCarModel(CarModelDto carBrandDto) {
        CarModel carModel = CarModel.builder()
                .id(carBrandDto.getId())
                .model(carBrandDto.getModel())
                .year(carBrandDto.getYear())
                .build();

        if (carBrandDto.getCarBrand() != null) {
            carModel.setCarBrand(CarBrandMapper.toCarBrand(carBrandDto.getCarBrand()));
        }

        return carModel;
    }
}
