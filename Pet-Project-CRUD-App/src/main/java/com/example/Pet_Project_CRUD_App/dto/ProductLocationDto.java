package com.example.Pet_Project_CRUD_App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductLocationDto {
    private Long id;
    private String productName;
    private String categoryName;
    private Integer warehousePlace;
}