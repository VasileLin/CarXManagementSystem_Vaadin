package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.CarBrand;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarBrandRepository extends JpaRepository<CarBrand,Integer> {

    @Query("SELECT c FROM CarBrand c LEFT JOIN FETCH c.carModels")
    @NonNull List<CarBrand> findAll();

}
