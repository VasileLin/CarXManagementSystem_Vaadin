package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.EmployerDto;
import com.vmoon.carx.dto.ServiceDto;
import com.vmoon.carx.entities.Employer;
import com.vmoon.carx.entities.Service;
import com.vmoon.carx.mappers.EmployerMapper;
import com.vmoon.carx.mappers.ServiceMapper;
import com.vmoon.carx.repositories.ServiceRepository;
import com.vmoon.carx.services.ServicesService;
import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService {
    private static final Logger logger = LoggerFactory.getLogger(ServicesServiceImpl.class);
    private final ServiceRepository serviceRepository;

    @Override
    public @NonNull Page<ServiceDto> allServices(@NonNull Pageable pageable) {
        return serviceRepository.findAll(pageable)
                .map(ServiceMapper.INSTANCE::toServiceDto);
    }

    @Override
    public List<ServiceDto> allServices() {
        return serviceRepository.findAll().stream().map(ServiceMapper.INSTANCE::toServiceDto).toList();
    }

    @Override
    public void saveService(ServiceDto serviceDto) {
        if (serviceDto != null) {
            serviceRepository.save(ServiceMapper.INSTANCE.toService(serviceDto));
        }
    }

    @Override
    public long count() {
        return serviceRepository.count();
    }

    @Override
    public Page<ServiceDto> searchService(String value, Pageable pageable) {
        Specification<Service> spec = textInAllColumns(value);
        Page<Service> page = serviceRepository.findAll(spec, pageable);
        return page.map(ServiceMapper.INSTANCE::toServiceDto);
    }

    @Override
    public long countSearchResults(String text) {
        return serviceRepository.count(textInAllColumns(text));
    }

    @Override
    public int countDeleted() {
       return serviceRepository.countDeleted();
    }

    @Override
    public Page<ServiceDto> allDeletedServices(Pageable pageable) {
        logger.info("All deleted services fetched successfully");
        return serviceRepository.listDeleted(pageable).map(ServiceMapper.INSTANCE::toServiceDto);
    }

    @Override
    public Page<ServiceDto> searchService(String value, Pageable pageable, boolean isDeleted) {
        Specification<Service> spec = textInAllColumns(value,isDeleted);
        Page<Service> page = serviceRepository.findAll(spec, pageable);
        return page.map(ServiceMapper.INSTANCE::toServiceDto);
    }

    public static Specification<Service> textInAllColumns(String text,boolean isDeleted) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        final String finalText = "%" + text.toLowerCase() + "%";
        return (root, query, cb) ->
        {
            Predicate textCondition = cb.or(
                    cb.like(cb.lower(root.get("name")), finalText)
            );

            Predicate isDeletedCondition = cb.equal(root.get("isDeleted"), isDeleted);
            return cb.and(textCondition, isDeletedCondition);

        };


    }


    public static Specification<Service> textInAllColumns(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        final String finalText = "%" + text.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), finalText));
    }
}
