package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.CarBrandDto;
import com.vmoon.carx.mappers.CarBrandMapper;
import com.vmoon.carx.repositories.CarBrandRepository;
import com.vmoon.carx.services.CarBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarBrandsService implements CarBrandService {

    private final CarBrandRepository carBrandRepository;
    @Override
    public List<CarBrandDto> allBrands() {
        return carBrandRepository.findAll()
                .stream()
                .map(CarBrandMapper::toCarBrandDtoWithModels)
                .toList();
    }

    @Override
    public void saveBrand(CarBrandDto carBrandDto) {
        if (carBrandDto != null) {
            carBrandRepository.save(CarBrandMapper.toCarBrand(carBrandDto));
        }
    }

    @Override
    public List<CarBrandDto> searchBrands(String value) {
        return List.of();
    }
}
