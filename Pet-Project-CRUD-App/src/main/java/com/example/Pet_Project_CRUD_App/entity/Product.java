package com.example.Pet_Project_CRUD_App.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(name = "product_price", nullable = false)
    private Double productPrice;

    @Column(nullable = false)
    private Integer quantity = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_place_id", nullable = false)
    private WarehousePlace warehousePlace;

    // Геттер для названия категории
    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }

    // Геттер для номера места
    public Integer getPlaceNumber() {
        return warehousePlace != null ? warehousePlace.getPlaceNumber() : null;
    }

    //  методы для установки связей
    public void setCategory(Category category) {
        this.category = category;
        if (category != null && !category.getProducts().contains(this)) {
            category.getProducts().add(this);
        }
    }

    public void setWarehousePlace(WarehousePlace warehousePlace) {
        this.warehousePlace = warehousePlace;
        if (warehousePlace != null && !warehousePlace.getProducts().contains(this)) {
            warehousePlace.getProducts().add(this);
        }
    }
}