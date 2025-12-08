package com.example.Pet_Project_CRUD_App.dto;

import com.example.Pet_Project_CRUD_App.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorWithData {
    private String errorMessage;
    private List<Warehouse> products;
}
