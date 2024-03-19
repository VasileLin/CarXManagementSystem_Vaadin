package com.vmoon.carx.services;

import com.vmoon.carx.dto.CustomerDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    @NonNull Page<CustomerDto> allCustomers(@NonNull Pageable pageable);

    List<CustomerDto> listCustomers();
    void saveCustomer(CustomerDto customer);
    long count();
    Page<CustomerDto> searchCustomers(String value, Pageable pageable);
    long countSearchResults(String value);
}
