package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.ServiceDto;
import com.vmoon.carx.entities.Service;
import com.vmoon.carx.mappers.ServiceMapper;
import com.vmoon.carx.repositories.ServiceRepository;
import com.vmoon.carx.services.ServicesService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService {

    private final ServiceRepository serviceRepository;

    @Override
    public @NonNull Page<ServiceDto> allServices(@NonNull Pageable pageable) {
        return serviceRepository.findAll(pageable).map(ServiceMapper::toServiceDto);
    }

    @Override
    public List<ServiceDto> allServices() {
        return serviceRepository.findAll().stream().map(ServiceMapper::toServiceDto).toList();
    }

    @Override
    public void saveService(ServiceDto serviceDto) {
        if (serviceDto != null) {
            serviceRepository.save(ServiceMapper.toService(serviceDto));
        }
    }

    @Override
    public long count() {
        return serviceRepository.count();
    }

    @Override
    public Page<ServiceDto> searchService(String value, Pageable pageable) {
        return null;
    }

    @Override
    public long countSearchResults(String text) {
        return serviceRepository.count(textInAllColumns(text));
    }

    public static Specification<Service> textInAllColumns(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        final String finalText = "%" + text.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), finalText),
                cb.like(cb.lower(root.get("price")), finalText));
    }
}
