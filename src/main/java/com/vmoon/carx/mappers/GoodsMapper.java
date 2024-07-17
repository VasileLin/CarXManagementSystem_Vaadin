package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.AcquisitionDto;
import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.entities.Goods;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GoodsMapper {
    GoodsMapper INSTANCE = Mappers.getMapper(GoodsMapper.class);

    @Mapping(source = "name", target = "costName")
    GoodsDto toGoodsDto(Goods goods, @Context CycleAvoidingMappingContext context);

    @Mapping(source = "costName", target = "name")
    Goods toGoods(GoodsDto goodsDto, @Context CycleAvoidingMappingContext context);

    AcquisitionDto toAcquisitionDto(GoodsDto goodsDto);
}
