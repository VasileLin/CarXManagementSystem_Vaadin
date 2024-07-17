package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CashDto;
import com.vmoon.carx.dto.CashGridDto;
import com.vmoon.carx.entities.Cash;
import com.vmoon.carx.entities.Customer;
import com.vmoon.carx.entities.Goods;
import com.vmoon.carx.entities.Service;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface CashMapper {
    CashMapper INSTANCE = Mappers.getMapper(CashMapper.class);

    CashDto toCashDto(Cash cash, @Context CycleAvoidingMappingContext context);

    Cash toCash(CashDto cashDto, @Context CycleAvoidingMappingContext context);

    CashGridDto toCashGridDto(Cash cashDto, @Context CycleAvoidingMappingContext context);

}
