package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CarModelDto;
import com.vmoon.carx.entities.CarModel;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
public interface CarModelMapper {

    CarModelMapper INSTANCE = Mappers.getMapper(CarModelMapper.class);


    CarModelDto toCarModelDto(CarModel carModel,@Context CycleAvoidingMappingContext context);

    CarModel toCarModel(CarModelDto carBrandDto,@Context CycleAvoidingMappingContext context);
}
