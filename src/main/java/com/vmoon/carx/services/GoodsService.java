package com.vmoon.carx.services;

import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.entities.Goods;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface GoodsService {
    @NonNull Page<GoodsDto> allGoods(@NonNull Pageable pageable);
    @NonNull List<GoodsDto> allGoods();
    GoodsDto saveGood(GoodsDto goodsDto);
    long count();
    Page<GoodsDto> searchGoods(String value, Pageable pageable);
    long countSearchResults(String text);
    void updateStock(long id, int newStock);
    Page<GoodsDto> allGoodsDate(PageRequest pageRequest, LocalDate value, LocalDate value1);
    long countDateResult(LocalDate value, LocalDate value1);
    Page<GoodsDto> fetchGoodsForCategoryAndBrand(int categoryId, int brandId,PageRequest pageRequest);
    long countSearchResults(int categoryId, int brandId);
}
