package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.Cash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashRepository extends JpaRepository<Cash, Long> {

}
