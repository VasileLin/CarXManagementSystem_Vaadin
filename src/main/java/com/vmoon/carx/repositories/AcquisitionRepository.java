package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.Acquisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcquisitionRepository extends JpaRepository<Acquisition, Integer> {
}
