package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.GoodsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsCategoryRepository extends JpaRepository<GoodsCategory,Integer> {
}
