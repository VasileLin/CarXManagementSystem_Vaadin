package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.entities.Goods;
import com.vmoon.carx.mappers.GoodsMapper;
import com.vmoon.carx.repositories.GoodsRepository;
import com.vmoon.carx.services.GoodsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;

    @Override
    public @NonNull Page<GoodsDto> allGoods(@NonNull Pageable pageable) {
        return goodsRepository.findAll(pageable).map(GoodsMapper::toGoodsDto);
    }

    @Override
    public @NonNull List<GoodsDto> allGoods() {
        return goodsRepository.findAll().stream().map(GoodsMapper::toGoodsDto).toList();
    }

    @Override
    public GoodsDto saveGood(GoodsDto goodsDto) {
        if (goodsDto != null) {
          return GoodsMapper.toGoodsDto(goodsRepository.save(GoodsMapper.toGoods(goodsDto)));
        }
        return null;
    }

    @Override
    public long count() {
        return goodsRepository.count();
    }

    @Override
    public Page<GoodsDto> searchGoods(String value, Pageable pageable) {
        return null;
    }

    @Override
    public long countSearchResults(String text) {
        return 0;
    }

    @Override
    public long countSearchResults(int categoryId, int brandId) {
        return goodsRepository.countAllByCategoryIdAndCarBrandId(categoryId, brandId);
    }

    @Override
    public void updateStock(long id, int newStock) {
        Optional<Goods> goodsDto = goodsRepository.findById(id);
        goodsDto.ifPresent(goods -> {
            goods.setStock(newStock);
            goodsRepository.save(goods);
        });
    }

    @Override
    public Page<GoodsDto> allGoodsDate(PageRequest pageRequest, LocalDate fromValue, LocalDate toValue) {
        return goodsRepository.findAllByDate(pageRequest,fromValue,toValue)
                .map(GoodsMapper::toGoodsDto);
    }

    @Override
    public long countDateResult(LocalDate fromValue, LocalDate toValue) {
        return goodsRepository.countAllByDate(fromValue,toValue);
    }

    @Override
    public Page<GoodsDto> fetchGoodsForCategoryAndBrand(int categoryId, int brandId,PageRequest pageRequest) {
        return goodsRepository.findAllByCategoryIdAndCarBrandId(categoryId,brandId,pageRequest)
         .map(GoodsMapper::toGoodsDto);
    }
}
