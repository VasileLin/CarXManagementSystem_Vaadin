package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CustomerDto;
import com.vmoon.carx.entities.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public static CustomerDto mapToCustomerDto(Customer customer) {
        CustomerDto customerDto = CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .carModel(CarModelMapper.toCarModelDto(customer.getCarModel()))
                .carNumber(customer.getCarNumber())
                .isDeleted(customer.isDeleted())
                .build();

        if (customer.getCarBrand() != null) {
            customerDto.setCarBrand(CarBrandMapper.toCarBrandDto(customer.getCarBrand()));
        }

        if (customer.getCarModel() != null) {
            customerDto.setCarModel(CarModelMapper.toCarModelDto(customer.getCarModel()));
        }

        return customerDto;
    }

    public static Customer mapToCustomer(CustomerDto customerDto) {
        Customer customer = Customer.builder()
                .id(customerDto.getId())
                .name(customerDto.getName())
                .phone(customerDto.getPhone())
                .email(customerDto.getEmail())
                .carModel(CarModelMapper.toCarModel(customerDto.getCarModel()))
                .carNumber(customerDto.getCarNumber())
                .isDeleted(customerDto.isDeleted())
                .build();

        if (customerDto.getCarBrand() != null) {
            customer.setCarBrand(CarBrandMapper.toCarBrand(customerDto.getCarBrand()));
        }

        if (customerDto.getCarModel() != null) {
            customer.setCarModel(CarModelMapper.toCarModel(customerDto.getCarModel()));
        }

        return customer;
    }

}
