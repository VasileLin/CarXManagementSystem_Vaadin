package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.CarModelDto;
import com.vmoon.carx.mappers.CarModelMapper;
import com.vmoon.carx.repositories.CarModelRepository;
import com.vmoon.carx.services.CarModelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CarModelServiceImpl implements CarModelService {

    private final CarModelRepository carModelRepository;

    public CarModelServiceImpl(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    @Override
    public List<CarModelDto> getCarModelsByBrandId(int brandId) {
        return carModelRepository.findAllByCarBrandId(brandId)
                .stream()
                .map(CarModelMapper::toCarModelDto)
                .toList();
    }

    @Override
    public void saveModel(CarModelDto carModelDto) {
        if (carModelDto != null) {
            carModelRepository.save(CarModelMapper.toCarModel(carModelDto));
        }
    }
}
