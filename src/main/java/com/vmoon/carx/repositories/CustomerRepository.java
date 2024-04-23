package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>, JpaSpecificationExecutor<Customer> {

    @EntityGraph(attributePaths = {"carBrand","carModel"})
    @NonNull
    Page<Customer> findAll(@NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"carBrand","carModel"})
    @NonNull
    List<Customer> findAll();
}
