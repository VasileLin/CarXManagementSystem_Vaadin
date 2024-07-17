package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.GoodsDto;
import com.vmoon.carx.entities.CarBrand;
import com.vmoon.carx.entities.CarModel;
import com.vmoon.carx.entities.Goods;
import com.vmoon.carx.mappers.CycleAvoidingMappingContext;
import com.vmoon.carx.mappers.GoodsMapper;
import com.vmoon.carx.repositories.GoodsRepository;
import com.vmoon.carx.services.GoodsService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public @NonNull List<GoodsDto> allGoods() {
        return goodsRepository.findAll().stream().map(e -> GoodsMapper.INSTANCE.toGoodsDto(e,new CycleAvoidingMappingContext())).toList();
    }

    @Override
    public GoodsDto saveGood(GoodsDto goodsDto) {
        if (goodsDto != null) {
          return GoodsMapper.INSTANCE.toGoodsDto(
                  goodsRepository.save(GoodsMapper.INSTANCE.toGoods(goodsDto,new CycleAvoidingMappingContext()))
                  ,new CycleAvoidingMappingContext());
        }
        return null;
    }

    @Override
    public long count() {
        return goodsRepository.count();
    }

    @Override
    public Page<GoodsDto> searchGoods(int categoryId, int brandId, String searchText, Pageable pageable) {
        Specification<Goods> combinedSpec = byCategoryBrandAndText(categoryId, brandId, searchText);
        return goodsRepository.findAll(combinedSpec, pageable).map(e -> GoodsMapper.INSTANCE.toGoodsDto(e,new CycleAvoidingMappingContext()));

    }

    @Override
    public long countSearchResults(int categoryId, int brandId, String searchText) {
        Specification<Goods> combinedSpec = byCategoryBrandAndText(categoryId, brandId, searchText);
        return goodsRepository.count(combinedSpec);
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
                .map(e -> GoodsMapper.INSTANCE.toGoodsDto(e,new CycleAvoidingMappingContext()));
    }

    @Override
    public long countDateResult(LocalDate fromValue, LocalDate toValue) {
        return goodsRepository.countAllByDate(fromValue,toValue);
    }

    @Override
    public Page<GoodsDto> fetchGoodsForCategoryAndBrand(int categoryId, int brandId,PageRequest pageRequest) {
        return goodsRepository.findAllByCategoryIdAndCarBrandId(categoryId,brandId,pageRequest)
         .map(e -> GoodsMapper.INSTANCE.toGoodsDto(e,new CycleAvoidingMappingContext()));
    }

    public static Specification<Goods> byCategoryBrandAndText(int categoryId, int brandId, String searchText) {
        return (root, query, cb) -> {
            Predicate categoryPredicate = cb.equal(root.get("category").get("id"), categoryId);
            Predicate brandPredicate = cb.equal(root.get("carBrand").get("id"), brandId);
            Predicate searchPredicate = textInAllColumns(searchText).toPredicate(root, query, cb);

            if (searchPredicate != null) {
                return cb.and(categoryPredicate, brandPredicate, searchPredicate);
            } else {
                return cb.and(categoryPredicate, brandPredicate);
            }
        };
    }

    public static Specification<Goods> textInAllColumns(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        final String finalText = "%" + text.toLowerCase() + "%";
        return (root, query, cb) -> {

            Join<Goods, CarBrand> carBrandJoin = root.join("carBrand");
            Join<Goods, CarModel> compatibleModelsJoin = root.join("compatibleModels", JoinType.LEFT);
            query.distinct(true);

            return cb.or(
                    cb.like(cb.lower(root.get("name")), finalText),
                    cb.like(cb.lower(carBrandJoin.get("brand")), finalText),
                    cb.like(cb.lower(root.get("carModel")), finalText),
                    cb.like(cb.lower(compatibleModelsJoin.get("model")), finalText)
            );
        };
    }
}
