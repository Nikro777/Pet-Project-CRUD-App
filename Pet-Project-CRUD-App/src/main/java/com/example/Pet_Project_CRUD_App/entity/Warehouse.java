package com.example.Pet_Project_CRUD_App.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "warehouses_table", schema = "public")
@Data
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String productName;
    private Integer warehousePlace;
    private Double productPrice;
    private Integer quantity;
}
