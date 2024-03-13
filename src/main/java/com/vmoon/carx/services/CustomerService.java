package com.vmoon.carx.services;

import com.vmoon.carx.dto.CustomerDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    @NonNull Page<CustomerDto> allCustomers(@NonNull Pageable pageable);
    void saveCustomer(CustomerDto customer);
    long count();
    Page<CustomerDto> searchCustomers(String value, Pageable pageable);
    long countSearchResults(String value);
}
