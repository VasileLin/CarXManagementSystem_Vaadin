package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.EmployerDto;
import com.vmoon.carx.entities.Employer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
public interface EmployerMapper {

    EmployerMapper INSTANCE = Mappers.getMapper(EmployerMapper.class);

    EmployerDto mapToEmployerDto(Employer employer);

    Employer mapToEmployer(EmployerDto employerDto);
}
