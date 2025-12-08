package com.example.Pet_Project_CRUD_App.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "warehouse_places")
@Data
public class WarehousePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_number", unique = true, nullable = false)
    private Integer placeNumber;

    @OneToMany(mappedBy = "warehousePlace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public WarehousePlace() {}

    public WarehousePlace(Integer placeNumber) {
        this.placeNumber = placeNumber;
    }
}