package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.CustomerDto;
import com.vmoon.carx.entities.Customer;
import com.vmoon.carx.mappers.CustomerMapper;
import com.vmoon.carx.repositories.CustomerRepository;
import com.vmoon.carx.services.CustomerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public @NonNull Page<CustomerDto> allCustomers(@NonNull Pageable pageable) {
        return customerRepository.findAll(pageable).map(CustomerMapper::mapToCustomerDto);
    }

    @Override
    public void saveCustomer(CustomerDto customer) {
        if (customer != null) {
            customerRepository.save(CustomerMapper.mapToCustomer(customer));
        }
    }

    @Override
    public long count() {
        return customerRepository.count();
    }

    @Override
    public Page<CustomerDto> searchCustomers(String value, Pageable pageable) {
        Specification<Customer> specification = textInAllColumns(value);
        Page<Customer> page = customerRepository.findAll(specification, pageable);
        return page.map(CustomerMapper::mapToCustomerDto);
    }

    @Override
    public long countSearchResults(String value) {
        return customerRepository.count(textInAllColumns(value));
    }

    public static Specification<Customer> textInAllColumns(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        final String finalText = "%" + text.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), finalText),
                cb.like(cb.lower(root.get("phone")), finalText),
                cb.like(cb.lower(root.get("carNumber")), finalText),
                cb.like(cb.lower(root.get("carModel")), finalText),
                cb.like(cb.lower(root.get("email")), finalText));

    }
}
