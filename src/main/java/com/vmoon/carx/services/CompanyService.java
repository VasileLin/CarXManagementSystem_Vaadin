package com.vmoon.carx.services;

import com.vmoon.carx.dto.CompanyDto;

import java.util.List;

public interface CompanyService {
    void saveCompany(CompanyDto companyDto);
    List<CompanyDto> getAllCompanies();
}
