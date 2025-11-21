package com.example.Pet_Project_CRUD_App.dto;

import com.example.Pet_Project_CRUD_App.entity.Warehouse;
import lombok.Data;

import java.util.List;

@Data
public class ErrorWithData {
    private String errorMessage;
    private List<Warehouse> products;
}
