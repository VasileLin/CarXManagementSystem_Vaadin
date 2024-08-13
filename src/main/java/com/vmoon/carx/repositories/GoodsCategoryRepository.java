package com.vmoon.carx.repositories;

import com.vmoon.carx.dto.GoodsCategoryDto;
import com.vmoon.carx.entities.Employer;
import com.vmoon.carx.entities.GoodsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsCategoryRepository extends JpaRepository<GoodsCategory,Integer>, JpaSpecificationExecutor<GoodsCategory> {

    @NonNull
    @Query("SELECT c FROM GoodsCategory c " +
            "WHERE c.isDeleted = :isDeleted")
    Page<GoodsCategory> allCategories(Pageable pageable, boolean isDeleted);

    @Query("SELECT count(c) FROM GoodsCategory c WHERE c.isDeleted = :isDeleted")
    long count(boolean isDeleted);
}
