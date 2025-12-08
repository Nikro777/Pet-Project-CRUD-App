package com.example.Pet_Project_CRUD_App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponseDto {

    private Long id;
    private String category;
    private String productName;
    private Double productPrice;
}