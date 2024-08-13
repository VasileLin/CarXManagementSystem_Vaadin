package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.GoodsCategoryDto;
import com.vmoon.carx.entities.GoodsCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GoodsCategoryMapper {

    GoodsCategoryMapper INSTANCE = Mappers.getMapper(GoodsCategoryMapper.class);

    GoodsCategoryDto toGoodsCategoryDto(GoodsCategory goodsCategory);

    GoodsCategory toGoodsCategory(GoodsCategoryDto category);
}
