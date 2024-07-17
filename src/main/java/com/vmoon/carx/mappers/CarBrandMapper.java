package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CarBrandDto;
import com.vmoon.carx.entities.CarBrand;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarBrandMapper {

    CarBrandMapper INSTANCE = Mappers.getMapper(CarBrandMapper.class);

    CarBrandDto toCarBrandDtoWithModels(CarBrand carBrand,@Context CycleAvoidingMappingContext context);

    CarBrand toCarBrand(CarBrandDto carBrandDto,@Context CycleAvoidingMappingContext context);
}
