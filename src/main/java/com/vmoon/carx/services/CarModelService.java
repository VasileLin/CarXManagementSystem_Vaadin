package com.vmoon.carx.services;

import com.vmoon.carx.dto.CarModelDto;

import java.util.List;

public interface CarModelService {

    List<CarModelDto> getCarModelsByBrandId(int brandId);

    void saveModel(CarModelDto carModelDto);
}
