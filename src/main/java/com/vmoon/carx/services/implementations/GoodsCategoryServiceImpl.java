package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.GoodsCategoryDto;
import com.vmoon.carx.mappers.GoodsCategoryMapper;
import com.vmoon.carx.repositories.GoodsCategoryRepository;
import com.vmoon.carx.services.GoodsCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

    private final GoodsCategoryRepository goodsCategoryRepository;

    @Override
    public List<GoodsCategoryDto> getAllCategories() {
        return goodsCategoryRepository.findAll()
                .stream()
                .map(GoodsCategoryMapper.INSTANCE::toGoodsCategoryDto)
                .toList();
    }

}
