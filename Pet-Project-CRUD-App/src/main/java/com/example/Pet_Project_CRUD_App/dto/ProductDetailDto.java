package com.example.Pet_Project_CRUD_App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDto {
    private Long id;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private String categoryName;
    private Integer warehousePlace;
    private Double totalValue;
}