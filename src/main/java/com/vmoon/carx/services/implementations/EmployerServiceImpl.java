package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.EmployerDto;
import com.vmoon.carx.entities.Employer;
import com.vmoon.carx.mappers.EmployerMapper;
import com.vmoon.carx.repositories.EmployerRepository;
import com.vmoon.carx.services.EmployerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;


    @Override
    public Employer saveEmployer(EmployerDto employee) {
        if (employee != null) {
            return employerRepository.save(EmployerMapper.mapToEmployer(employee));
        }
        return null;
    }

    @Override
    public @NonNull Page<EmployerDto> allEmployers(@NonNull Pageable pageable) {
        return employerRepository.list(pageable).map(EmployerMapper::mapToEmployerDto);
    }

    @Override
    public long count() {
        return employerRepository.count();
    }

}
