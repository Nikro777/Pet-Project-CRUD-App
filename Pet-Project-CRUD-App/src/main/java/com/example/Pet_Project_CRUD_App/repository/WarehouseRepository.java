package com.example.Pet_Project_CRUD_App.repository;

import com.example.Pet_Project_CRUD_App.dto.CategoryStatsDto;
import com.example.Pet_Project_CRUD_App.entity.Warehouse;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByCategory(String category);
    List<Warehouse> findByProductNameContainingIgnoreCase(String productName);
    List<Warehouse> findByWarehousePlace(Integer warehousePlace);

    @Query("SELECT NEW com.example.Pet_Project_CRUD_App.dto.CategoryStatsDto(w.category, COUNT(w)) " +
            "FROM Warehouse w WHERE w.category = :category GROUP BY w.category")
    List<CategoryStatsDto> getCategoryStats(String category);

    @Query("SELECT SUM(w.productPrice * w.quantity) FROM Warehouse w")
    Double totalSumAllProducts();

    List<Warehouse> findByProductPriceBetween(Integer min, Integer max);

    List<Warehouse> findAll(Sort sort);

    boolean existsByProductNameIgnoreCase(String productName);

    Optional<Warehouse> findById(Long id);
}
