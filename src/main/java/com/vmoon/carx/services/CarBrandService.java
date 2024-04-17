package com.vmoon.carx.services;

import com.vmoon.carx.dto.CarBrandDto;

import java.util.List;

public interface CarBrandService {

    List<CarBrandDto> allBrands();

    void saveBrand(CarBrandDto carBrandDto);

    List<CarBrandDto> searchBrands(String value);
}
