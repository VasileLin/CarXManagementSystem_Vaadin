package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.EmployerDto;
import com.vmoon.carx.entities.Employer;
import com.vmoon.carx.mappers.EmployerMapper;
import com.vmoon.carx.repositories.EmployerRepository;
import com.vmoon.carx.services.EmployerService;
import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployerServiceImpl implements EmployerService {
    private static final Logger logger = LoggerFactory.getLogger(EmployerServiceImpl.class);
    private final EmployerRepository employerRepository;


    @Override
    public void saveEmployer(EmployerDto employee) {
        if (employee != null) {
            logger.info("Saving employer: {}", employee);
            employerRepository.save(EmployerMapper.INSTANCE.mapToEmployer(employee));
        }
    }



    @Override
    public @NonNull Page<EmployerDto> allEmployers(@NonNull Pageable pageable) {
        logger.info("All employers fetched successfully");
        return employerRepository.list(pageable).map(EmployerMapper.INSTANCE::mapToEmployerDto);
    }

    @Override
    public @NonNull Page<EmployerDto> allDeletedEmployers(@NonNull Pageable pageable) {
        logger.info("All deleted employers fetched successfully");
        return employerRepository.listDeleted(pageable).map(EmployerMapper.INSTANCE::mapToEmployerDto);
    }

    @Override
    public long count(boolean isDeleted) {
        return employerRepository.count(allEmployers(isDeleted));
    }

    @Override
    public Page<EmployerDto> searchEmployers(String searchText, Pageable pageable,boolean isDeleted) {
        Specification<Employer> spec = textInAllColumns(searchText,isDeleted);
        Page<Employer> page = employerRepository.findAll(spec, pageable);
        return page.map(EmployerMapper.INSTANCE::mapToEmployerDto);
    }

    @Override
    public long countSearchResults(String text,boolean isDeleted) {
        return employerRepository.count(textInAllColumns(text,isDeleted));
    }

    public static Specification<Employer> textInAllColumns(String text,boolean isDeleted) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        final String finalText = "%" + text.toLowerCase() + "%";
        return (root, query, cb) ->
        {
                Predicate textCondition = cb.or(
                    cb.like(cb.lower(root.get("fullName")), finalText),
                    cb.like(cb.lower(root.get("email")), finalText),
                    cb.like(cb.lower(root.get("address")), finalText),
                    cb.like(cb.lower(root.get("phone")), finalText)
            );

            Predicate isDeletedCondition = cb.equal(root.get("isDeleted"), isDeleted);
            return cb.and(textCondition, isDeletedCondition);

        };


    }

    public static Specification<Employer> allEmployers(boolean isDeleted) {
        return (root, query, cb) ->
                cb.equal(root.get("isDeleted"), isDeleted);
    }

}
