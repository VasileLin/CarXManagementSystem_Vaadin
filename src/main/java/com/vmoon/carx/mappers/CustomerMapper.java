package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CustomerDto;
import com.vmoon.carx.entities.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public static CustomerDto mapToCustomerDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .carModel(customer.getCarModel())
                .carNumber(customer.getCarNumber())
                .build();
    }

    public static Customer mapToCustomer(CustomerDto customerDto) {
        return Customer.builder()
                .id(customerDto.getId())
                .name(customerDto.getName())
                .phone(customerDto.getPhone())
                .email(customerDto.getEmail())
                .carModel(customerDto.getCarModel())
                .carNumber(customerDto.getCarNumber())
                .build();
    }

}
