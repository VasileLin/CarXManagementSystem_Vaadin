package com.vmoon.carx.services;

import com.vmoon.carx.dto.GoodsDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GoodsService {
    @NonNull Page<GoodsDto> allGoods(@NonNull Pageable pageable);
    @NonNull List<GoodsDto> allGoods();
    void saveGood(GoodsDto employee);
    long count();
    Page<GoodsDto> searchGoods(String value, Pageable pageable);
    long countSearchResults(String text);

    void updateStock(long id, int newStock);
}