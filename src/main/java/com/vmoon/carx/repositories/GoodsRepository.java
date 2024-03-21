package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.Cash;
import com.vmoon.carx.entities.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface GoodsRepository extends JpaRepository<Goods, Long>, JpaSpecificationExecutor<Goods> {
    @Query("SELECT g FROM Goods g " +
            "WHERE g.date >= :fromValue AND g.date <= :toValue")
    Page<Goods> findAllByDate(Pageable pageable, @Param("fromValue") LocalDate fromValue, @Param("toValue")LocalDate toValue);

    @Query("SELECT count(g) FROM Goods g " +
            "WHERE g.date >= :fromValue AND g.date <= :toValue")
    long countAllByDate(@Param("fromValue")LocalDate fromValue, @Param("toValue")LocalDate toValue);
}
