package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.CustomerDto;
import com.vmoon.carx.entities.Customer;
import com.vmoon.carx.mappers.CustomerMapper;
import com.vmoon.carx.repositories.CustomerRepository;
import com.vmoon.carx.services.CustomerService;
import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public @NonNull Page<CustomerDto> allCustomers(@NonNull Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(CustomerMapper::mapToCustomerDto);
    }

    @Override
    public @NonNull Page<CustomerDto> allDeletedCustomers(@NonNull Pageable pageable) {
        return customerRepository.findAllDeleted(pageable)
                .map(CustomerMapper::mapToCustomerDto);
    }

    @Override
    public List<CustomerDto> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::mapToCustomerDto)
                .filter(customerDto -> !customerDto.isDeleted())
                .toList();
    }

    @Override
    public void saveCustomer(CustomerDto customer) {
        if (customer != null) {
            customerRepository.save(CustomerMapper.mapToCustomer(customer));
        }
    }

    @Override
    public long countDeleted() {
        return customerRepository.countDeleted();
    }

    @Override
    public long countCustomers() {
        return customerRepository.count();
    }

    @Override
    public Page<CustomerDto> searchCustomers(String value, Pageable pageable) {
        Specification<Customer> specification = textInAllColumns(value);
        Page<Customer> page = customerRepository.findAll(specification, pageable);
        return page.map(CustomerMapper::mapToCustomerDto);
    }

    @Override
    public Page<CustomerDto> searchDeletedCustomers(String value, Pageable pageable) {
        Specification<Customer> specification = textInAllColumnsDeleted(value);
        Page<Customer> page = customerRepository.findAll(specification, pageable);
        return page.map(CustomerMapper::mapToCustomerDto);
    }

    @Override
    public long countSearchResults(String value) {
        return customerRepository.count(textInAllColumns(value));
    }

    @Override
    public long countDeletedSearchResults(String value) {
        return customerRepository.count(textInAllColumnsDeleted(value));
    }

    public static Specification<Customer> textInAllColumns(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        final String finalText = "%" + text.toLowerCase() + "%";
        return (root, query, cb) -> {

            Predicate textCondition = cb.or(
                    cb.like(cb.lower(root.get("name")), finalText),
                    cb.like(cb.lower(root.get("phone")), finalText),
                    cb.like(cb.lower(root.get("email")), finalText)
            );

            Predicate isDeletedCondition = cb.equal(root.get("isDeleted"), false);

            return cb.and(textCondition, isDeletedCondition);
        };

    }

    public static Specification<Customer> textInAllColumnsDeleted(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        final String finalText = "%" + text.toLowerCase() + "%";
        return (root, query, cb) -> {

            Predicate textCondition = cb.or(
                    cb.like(cb.lower(root.get("name")), finalText),
                    cb.like(cb.lower(root.get("phone")), finalText),
                    cb.like(cb.lower(root.get("email")), finalText)
            );

            Predicate isDeletedCondition = cb.equal(root.get("isDeleted"), true);

            return cb.and(textCondition, isDeletedCondition);
        };
    }
}
