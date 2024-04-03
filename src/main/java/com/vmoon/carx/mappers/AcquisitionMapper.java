package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.AcquisitionDto;
import com.vmoon.carx.entities.Acquisition;
import org.springframework.stereotype.Component;

@Component
public class AcquisitionMapper {

    public static AcquisitionDto toAcquisitionDto(Acquisition acquisition) {


        return AcquisitionDto.builder()
                .id(acquisition.getId())
                .totalPrice(acquisition.getTotalPrice())
                .quantity(acquisition.getQuantity())
                .build();
    }

    public static Acquisition toAcquisition(AcquisitionDto acquisitionDto) {

        return Acquisition.builder()
                .id(acquisitionDto.getId())
                .totalPrice(acquisitionDto.getTotalPrice())
                .date(acquisitionDto.getDate())
                .quantity(acquisitionDto.getQuantity())
                .build();
    }
}
