package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.GoodsCategoryDto;
import com.vmoon.carx.entities.GoodsCategory;
import com.vmoon.carx.mappers.GoodsCategoryMapper;
import com.vmoon.carx.repositories.GoodsCategoryRepository;
import com.vmoon.carx.services.CategoriesService;
import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriesServiceImpl implements CategoriesService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriesServiceImpl.class);
    private final GoodsCategoryRepository goodsCategoryRepository;

    @Override
    public @NonNull Page<GoodsCategoryDto> allCategories(@NonNull Pageable pageable, boolean isDeleted) {
        return goodsCategoryRepository.allCategories(pageable, isDeleted)
                .map(GoodsCategoryMapper.INSTANCE::toGoodsCategoryDto);
    }

    public List<GoodsCategoryDto> getAllCategories() {
        return goodsCategoryRepository.findAll()
                .stream()
                .map(GoodsCategoryMapper.INSTANCE::toGoodsCategoryDto)
                .toList();
    }

    @Override
    public Page<GoodsCategoryDto> searchCategories(String value, Pageable pageable, boolean isDeleted) {
        Specification<GoodsCategory> spec = textInAllColumns(value,isDeleted);
        Page<GoodsCategory> page = goodsCategoryRepository.findAll(spec, pageable);
        return page.map(GoodsCategoryMapper.INSTANCE::toGoodsCategoryDto);
    }

    @Override
    public long countSearchResults(String text, boolean isDeleted) {
        return goodsCategoryRepository.count(textInAllColumns(text,isDeleted));
    }

    @Override
    public long count(boolean isDeleted) {
        return goodsCategoryRepository.count(isDeleted);
    }

    @Override
    public void saveCategory(GoodsCategoryDto categoryDto) {
        if (categoryDto != null) {
            logger.info("Saving category: {}", categoryDto.toString());
            goodsCategoryRepository.save(GoodsCategoryMapper.INSTANCE.toGoodsCategory(categoryDto));
        }
    }

    public static Specification<GoodsCategory> textInAllColumns(String text, boolean isDeleted) {
        if ((text == null) || text.isEmpty()) {
            return null;
        }

        final String finalText = "%" + text.toLowerCase() + "%";
        return (root, query, cb) ->
        {
            Predicate textCondition = cb.or(
                    cb.like(cb.lower(root.get("fullName")), finalText),
                    cb.like(cb.lower(root.get("email")), finalText),
                    cb.like(cb.lower(root.get("address")), finalText),
                    cb.like(cb.lower(root.get("phone")), finalText)
            );

            Predicate isDeletedCondition = cb.equal(root.get("isDeleted"), isDeleted);
            return cb.and(textCondition, isDeletedCondition);

        };
    }
}
