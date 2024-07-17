package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CashDto;
import com.vmoon.carx.dto.CashGridDto;
import com.vmoon.carx.entities.Cash;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CashMapper {
    CashMapper INSTANCE = Mappers.getMapper(CashMapper.class);

    CashDto toCashDto(Cash cash, @Context CycleAvoidingMappingContext context);

    Cash toCash(CashDto cashDto, @Context CycleAvoidingMappingContext context);

    CashGridDto toCashGridDto(Cash cashDto, @Context CycleAvoidingMappingContext context);

}
