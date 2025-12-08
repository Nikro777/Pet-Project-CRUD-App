package com.example.Pet_Project_CRUD_App.controller;

import com.example.Pet_Project_CRUD_App.entity.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Pet_Project_CRUD_App.service.WarehouseService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/db")
    public String testDatabase() {
        return "Database connection test endpoint";
    }

    @GetMapping("/connection")
    public String testConnection() {
        try {
            int count = warehouseService.getAllProducts().size();
            return "Total records in database: " + count;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Проверка кол-ва записей и первых трех записей
    @GetMapping("/debug")
    public String fullDebug() {
        try {
            List<Warehouse> warehouseList = warehouseService.getAllProducts();

            Integer sqlCount = warehouseList.size();

            List<String> firstRecords = new ArrayList<>();
            for (int i = 0; i < Math.min(3, warehouseList.size()); i++){
                firstRecords.add("Запись № " + (i+1) + " = " + warehouseList.get(i).toString());
            }
            return String.format("SQL Count: %d, First records: %s", sqlCount, firstRecords);
        } catch (Exception e) {
            return "Debug Error: " + e.getMessage();
        }
    }
}