package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.AcquisitionDto;
import com.vmoon.carx.entities.Acquisition;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AcquisitionMapper {

    AcquisitionMapper INSTANCE = Mappers.getMapper(AcquisitionMapper.class);
    AcquisitionDto toAcquisitionDto(Acquisition acquisition);
    Acquisition toAcquisition(AcquisitionDto acquisitionDto);
}
