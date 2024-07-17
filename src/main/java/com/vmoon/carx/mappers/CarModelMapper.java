package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CarModelDto;
import com.vmoon.carx.entities.CarModel;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarModelMapper {

    CarModelMapper INSTANCE = Mappers.getMapper(CarModelMapper.class);


    CarModelDto toCarModelDto(CarModel carModel,@Context CycleAvoidingMappingContext context);

    CarModel toCarModel(CarModelDto carBrandDto,@Context CycleAvoidingMappingContext context);
}
