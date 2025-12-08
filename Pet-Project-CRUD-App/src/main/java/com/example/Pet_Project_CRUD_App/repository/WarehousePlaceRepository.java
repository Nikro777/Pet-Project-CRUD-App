package com.example.Pet_Project_CRUD_App.repository;

import com.example.Pet_Project_CRUD_App.entity.WarehousePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehousePlaceRepository extends JpaRepository<WarehousePlace, Long> {

    WarehousePlace findByPlaceNumber(Integer placeNumber);

    boolean existsByPlaceNumber(Integer placeNumber);
}