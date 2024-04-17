package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarModelRepository extends JpaRepository<CarModel, Integer> {


    List<CarModel> findAllByCarBrandId(int brandId);

}
