package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.ServiceDto;
import com.vmoon.carx.entities.Service;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
public interface ServiceMapper {
     ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

     ServiceDto toServiceDto(Service service);

     Service toService(ServiceDto serviceDto);
}
