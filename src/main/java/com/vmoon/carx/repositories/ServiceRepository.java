package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.Service;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service,Integer>, JpaSpecificationExecutor<Service> {

    @Query("SELECT COUNT(*) FROM Service WHERE isDeleted = true")
    int countDeleted();

    @Query("SELECT e FROM Service e WHERE e.isDeleted = true")
    @NonNull
    Page<Service> listDeleted(Pageable pageable);

    @Query("SELECT e FROM Service e WHERE e.isDeleted = false")
    @NonNull Page<Service> findAll(@NonNull Pageable pageable);

    @Query("SELECT COUNT(*) FROM Service WHERE isDeleted = false")
    long count();
}
