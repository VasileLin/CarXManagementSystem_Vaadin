package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.CompanyDto;
import com.vmoon.carx.entities.Company;
import com.vmoon.carx.mappers.CompanyMapper;
import com.vmoon.carx.repositories.CompanyRepository;
import com.vmoon.carx.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public void saveCompany(CompanyDto companyDto) {
        List<Company> companies = companyRepository.findAll();

        if (companies.isEmpty()) {
            companyRepository.save(CompanyMapper.INSTANCE.toCompany(companyDto));
        } else {
            Company company = companies.get(0);
            company.setName(companyDto.getName());
            company.setAddress(companyDto.getAddress());
            company.setIban(companyDto.getIban());
            companyRepository.save(company);
        }
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(CompanyMapper.INSTANCE::toCompanyDto)
                .toList();
    }
}
