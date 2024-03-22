package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CompanyDto;
import com.vmoon.carx.entities.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public static CompanyDto toCompanyDto(Company company) {
        return CompanyDto.builder()
                .id(company.getId())
                .name(company.getName())
                .address(company.getAddress())
                .iban(company.getIban())
                .build();
    }

    public static Company toCompany(CompanyDto companyDto) {
        return Company.builder()
                .id(companyDto.getId())
                .name(companyDto.getName())
                .address(companyDto.getAddress())
                .iban(companyDto.getIban())
                .build();
    }
}
