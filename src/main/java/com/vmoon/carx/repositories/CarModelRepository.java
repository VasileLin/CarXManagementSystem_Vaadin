package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Integer> {

    List<CarModel> findAllByCarBrandId(int brandId);

}
