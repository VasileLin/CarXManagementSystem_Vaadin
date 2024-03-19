package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.ServiceDto;
import com.vmoon.carx.entities.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {
    public static ServiceDto toServiceDto(Service service) {
        return ServiceDto.builder()
                .id(service.getId())
                .name(service.getName())
                .price(service.getPrice())
                .build();
    }

    public static Service toService(ServiceDto serviceDto) {
        return Service.builder()
                .id(serviceDto.getId())
                .name(serviceDto.getName())
                .price(serviceDto.getPrice())
                .build();
    }
}
