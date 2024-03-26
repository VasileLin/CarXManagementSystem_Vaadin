package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.entities.Goods;
import org.springframework.stereotype.Component;

@Component
public class GoodsMapper {

    public static GoodsDto toGoodsDto(Goods goods) {
        return GoodsDto.builder()
                .id(goods.getId())
                .costName(goods.getName())
                .cost(goods.getCost())
                .date(goods.getDate())
                .stock(goods.getStock())
                .build();
    }

    public static Goods toGoods(GoodsDto goodsDto) {
        return Goods.builder()
                .id(goodsDto.getId())
                .name(goodsDto.getCostName())
                .cost(goodsDto.getCost())
                .date(goodsDto.getDate())
                .stock(goodsDto.getStock())
                .build();
    }
}
