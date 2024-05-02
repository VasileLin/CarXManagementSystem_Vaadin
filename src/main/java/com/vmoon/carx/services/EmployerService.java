package com.vmoon.carx.services;

import com.vmoon.carx.dto.EmployerDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployerService {

    @NonNull Page<EmployerDto> allEmployers(@NonNull Pageable pageable);
    @NonNull Page<EmployerDto> allDeletedEmployers(@NonNull Pageable pageable);
    void saveEmployer(EmployerDto employee);
    long count(boolean isDeleted);
    Page<EmployerDto> searchEmployers(String value, Pageable pageable,boolean isDeleted);
    long countSearchResults(String text,boolean isDeleted);
}
