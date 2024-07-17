package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CustomerDto;
import com.vmoon.carx.entities.Customer;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "carModel.carBrand", ignore = true)
    CustomerDto mapToCustomerDto(Customer customer, @Context CycleAvoidingMappingContext context);

    Customer mapToCustomer(CustomerDto customerDto, @Context CycleAvoidingMappingContext context);

}
