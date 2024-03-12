package com.vmoon.carx.services;

import com.vmoon.carx.dto.EmployerDto;
import com.vmoon.carx.entities.Employer;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface EmployerService {

    @NonNull Page<EmployerDto> allEmployers(@NonNull Pageable pageable);
    Employer saveEmployer(EmployerDto employee);

    long count();
}
