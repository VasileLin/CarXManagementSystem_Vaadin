package com.vmoon.carx.services;

import com.vmoon.carx.dto.EmployerDto;
import com.vmoon.carx.dto.ServiceDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicesService {

    @NonNull
    Page<ServiceDto> allServices(@NonNull Pageable pageable);

    List<ServiceDto> allServices();

    void saveService(ServiceDto serviceDto);

    long count();

    Page<ServiceDto> searchService(String value, Pageable pageable);

    long countSearchResults(String text);

    int countDeleted();

    Page<ServiceDto> allDeletedServices(Pageable pageable);

    Page<ServiceDto> searchService(String value, Pageable pageable, boolean isDeleted);
}
