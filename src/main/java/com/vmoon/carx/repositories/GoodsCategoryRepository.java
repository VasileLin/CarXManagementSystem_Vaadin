package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.GoodsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsCategoryRepository extends JpaRepository<GoodsCategory,Integer> {

    @NonNull
    List<GoodsCategory> findAll();
}
