package com.vmoon.carx.services;

import com.vmoon.carx.dto.GoodsCategoryDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoriesService {
    @NonNull
    Page<GoodsCategoryDto> allCategories(@NonNull Pageable pageable,boolean isDeleted);

    List<GoodsCategoryDto> getAllCategories();

    Page<GoodsCategoryDto> searchCategories(String value, Pageable pageable, boolean isDeleted);

    long countSearchResults(String value, boolean isDeleted);

    long count(boolean isDeleted);

    void saveCategory(GoodsCategoryDto categoryDto);
}
