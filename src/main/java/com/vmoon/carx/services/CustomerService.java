package com.vmoon.carx.services;

import com.vmoon.carx.dto.CustomerDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    @NonNull Page<CustomerDto> allCustomers(@NonNull Pageable pageable);
    @NonNull Page<CustomerDto> allDeletedCustomers(@NonNull Pageable pageable);
    List<CustomerDto> listCustomers();
    void saveCustomer(CustomerDto customer);
    long countDeleted();
    long countCustomers();
    Page<CustomerDto> searchCustomers(String value, Pageable pageable);
    Page<CustomerDto> searchDeletedCustomers(String value, Pageable pageable);
    long countDeletedSearchResults(String value);
    long countSearchResults(String value);
}
