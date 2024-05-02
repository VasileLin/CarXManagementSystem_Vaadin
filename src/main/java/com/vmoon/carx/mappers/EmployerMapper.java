package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.EmployerDto;
import com.vmoon.carx.entities.Employer;
import org.springframework.stereotype.Component;

@Component
public class EmployerMapper {

    public static EmployerDto mapToEmployerDto(Employer employer) {
        EmployerDto employerDto = EmployerDto.builder()
                .id(employer.getId())
                .fullName(employer.getFullName())
                .address(employer.getAddress())
                .phone(employer.getPhone())
                .email(employer.getEmail())
                .dateOfBirth(employer.getDateOfBirth())
                .isDeleted(employer.isDeleted())
                .build();

        if (employer.getRole() != null) {
            employerDto.setRole(RoleMapper.mapToRoleDto(employer.getRole()));
        }

        return employerDto;
    }


    public static Employer mapToEmployer(EmployerDto employerDto) {
        Employer employer = Employer.builder()
                .id(employerDto.getId())
                .fullName(employerDto.getFullName())
                .address(employerDto.getAddress())
                .phone(employerDto.getPhone())
                .email(employerDto.getEmail())
                .dateOfBirth(employerDto.getDateOfBirth())
                .isDeleted(employerDto.isDeleted())
                .build();

        if (employerDto.getRole() != null) {
            employer.setRole(RoleMapper.mapToRole(employerDto.getRole()));
        }

        return employer;
    }
}
