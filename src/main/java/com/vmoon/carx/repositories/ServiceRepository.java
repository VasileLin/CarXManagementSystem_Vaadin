package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service,Integer>, JpaSpecificationExecutor<Service> {
}
