package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.GoodsCategoryDto;
import com.vmoon.carx.entities.GoodsCategory;
import org.springframework.stereotype.Component;

@Component
public class GoodsCategoryMapper {

    public static GoodsCategoryDto toGoodsCategoryDto(GoodsCategory goodsCategory) {
           return GoodsCategoryDto.builder()
                .id(goodsCategory.getId())
                .name(goodsCategory.getName())
                .build();
    }

    public static GoodsCategory toGoods(GoodsCategoryDto category) {
        return GoodsCategory.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
