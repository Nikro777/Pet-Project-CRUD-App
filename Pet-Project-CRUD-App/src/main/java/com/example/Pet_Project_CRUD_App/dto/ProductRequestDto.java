package com.example.Pet_Project_CRUD_App.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductRequestDto {

    @NotBlank(message = "Название товара не может быть пустым")
    @Size(min = 2, max = 200, message = "Название должно быть от 2 до 200 символов")
    private String productName;

    @NotNull(message = "Цена не может быть пустой")
    @Positive(message = "Цена должна быть положительной")
    @DecimalMin(value = "0.01", message = "Цена должна быть не менее 0.01")
    private Double productPrice;

    @NotNull(message = "Количество не может быть пустым")
    @Min(value = 0, message = "Количество не может быть отрицательным")
    private Integer quantity;

    @NotBlank(message = "Категория не может быть пустой")
    private String categoryName;

    @NotNull(message = "Место на складе не может быть пустым")
    @Min(1) @Max(3)
    private Integer warehousePlace;
}