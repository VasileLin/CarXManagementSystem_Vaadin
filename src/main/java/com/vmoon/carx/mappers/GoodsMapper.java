package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.AcquisitionDto;
import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.entities.Goods;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GoodsMapper {

    public static GoodsDto toGoodsDto(Goods goods) {
        GoodsDto goodsDto = GoodsDto.builder()
                .id(goods.getId())
                .costName(goods.getName())
                .cost(goods.getCost())
                .date(goods.getDate())
                .stock(goods.getStock())
                .build();

        if (goods.getCategory() != null) {
            goodsDto.setCategory(GoodsCategoryMapper.toGoodsCategoryDto(goods.getCategory()));
        }

        if (goods.getCarBrand() != null) {
            goodsDto.setCarBrand(CarBrandMapper.toCarBrandDto(goods.getCarBrand()));
        }

        if (goods.getCompatibleModels() != null) {
            goodsDto.setCompatibleModels(goods.getCompatibleModels()
                    .stream()
                    .map(CarModelMapper::toCarModelDto)
                    .collect(Collectors.toSet()));
        }

        return goodsDto;
    }

    public static Goods toGoods(GoodsDto goodsDto) {
        Goods goods = Goods.builder()
                .id(goodsDto.getId())
                .name(goodsDto.getCostName())
                .cost(goodsDto.getCost())
                .date(goodsDto.getDate())
                .stock(goodsDto.getStock())
                .build();

        if (goodsDto.getCarBrand() != null) {
            goods.setCarBrand(CarBrandMapper.toCarBrand(goodsDto.getCarBrand()));
        }

        if (goodsDto.getCategory() != null) {
            goods.setCategory(GoodsCategoryMapper.toGoods(goodsDto.getCategory()));
        }

        if (goodsDto.getCompatibleModels() != null) {
            goods.setCompatibleModels(goodsDto.getCompatibleModels()
                    .stream()
                    .map(CarModelMapper::toCarModel)
                    .collect(Collectors.toSet()));
        }

        return goods;
    }

    public static AcquisitionDto toAcquisitionDto(GoodsDto goodsDto) {
        return AcquisitionDto.builder()
                .quantity(goodsDto.getQuantity())
                .totalPrice(goodsDto.getCost() * goodsDto.getStock())
                .date(goodsDto.getDate())
                .build();
    }
}
