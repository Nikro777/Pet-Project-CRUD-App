package com.example.Pet_Project_CRUD_App.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-db")
    public String testDatabase() {
        return "Database connection test endpoint";
    }
}