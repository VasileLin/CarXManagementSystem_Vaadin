package com.vmoon.carx.services;

import com.vmoon.carx.dto.EmployerDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployerService {

    @NonNull Page<EmployerDto> allEmployers(@NonNull Pageable pageable);
    void saveEmployer(EmployerDto employee);
    long count();

    Page<EmployerDto> searchEmployers(String value, Pageable pageable);

    long countSearchResults(String text);
}
