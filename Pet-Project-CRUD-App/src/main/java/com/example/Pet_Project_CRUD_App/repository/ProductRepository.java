package com.example.Pet_Project_CRUD_App.repository;

import com.example.Pet_Project_CRUD_App.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.category.name = :categoryName")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT p FROM Product p WHERE p.warehousePlace.placeNumber = :placeNumber")
    List<Product> findByWarehousePlace(@Param("placeNumber") Integer placeNumber);

    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:name%")
    List<Product> searchByProductName(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    @Query("SELECT p FROM Product p WHERE p.productPrice BETWEEN :min AND :max")
    List<Product> findByPriceBetween(@Param("min") Double min, @Param("max") Double max);
}