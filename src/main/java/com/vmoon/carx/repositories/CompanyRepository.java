package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {
}
