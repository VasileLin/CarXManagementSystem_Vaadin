package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.Cash;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CashRepository extends JpaRepository<Cash, Long> {

    @Query("SELECT c FROM Cash c " +
            "LEFT JOIN FETCH c.customer " +
            "WHERE c.date >= :fromValue AND c.date <= :toValue")
    Page<Cash> findAllByDate(Pageable pageable, @Param("fromValue")LocalDate fromValue, @Param("toValue")LocalDate toValue);

    @Query("SELECT count(c) FROM Cash c " +
            "WHERE c.date >= :fromValue AND c.date <= :toValue")
    long countAllByDate(@Param("fromValue")LocalDate fromValue, @Param("toValue")LocalDate toValue);
}
