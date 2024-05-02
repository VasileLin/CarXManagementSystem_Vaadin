package com.vmoon.carx.repositories;


import com.vmoon.carx.entities.Employer;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long>, JpaSpecificationExecutor<Employer> {
    @Query("SELECT e FROM Employer e LEFT JOIN FETCH e.role WHERE e.isDeleted = false")
    @NonNull Page<Employer> list(@NonNull Pageable pageable);

    @Query("SELECT e FROM Employer e LEFT JOIN FETCH e.role WHERE e.isDeleted = true")
    @NonNull Page<Employer> listDeleted(Pageable pageable);
}
