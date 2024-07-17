package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CompanyDto;
import com.vmoon.carx.entities.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDto toCompanyDto(Company company);

    Company toCompany(CompanyDto companyDto);
}
