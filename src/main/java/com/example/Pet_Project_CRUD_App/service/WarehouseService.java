package com.example.Pet_Project_CRUD_App.service;

import com.example.Pet_Project_CRUD_App.dto.CategoryStatsDto;
import com.example.Pet_Project_CRUD_App.dto.ErrorWithData;
import com.example.Pet_Project_CRUD_App.entity.Warehouse;
import com.example.Pet_Project_CRUD_App.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    // GET ALL
    public List<Warehouse> getAllProducts() {
        return warehouseRepository.findAll();
    }

    // GET BY ID
    public Optional<Warehouse> getProductById(Long id) {
        return warehouseRepository.findById(id);
    }

    // GET BY CATEGORY
    public List<Warehouse> getProductsByCategory(String category) {
        return warehouseRepository.findByCategory(category);
    }

    // CREATE
    public Warehouse createProduct(Warehouse warehouse) {

        if (warehouse.getCategory() == null
                || warehouse.getProductName() == null
                || warehouse.getWarehousePlace() == null
                || warehouse.getProductPrice() == null
                || warehouse.getQuantity() == null) {
            throw new RuntimeException("Заполнены не все поля");
        }
        if (warehouseRepository.existsByProductNameIgnoreCase(warehouse.getProductName())){
            throw new RuntimeException("Товар с таким названием уже существует: "
                    + warehouse.getProductName());
        }
        return warehouseRepository.save(warehouse);
    }

    // UPDATE
    public Warehouse updateProduct(Long id, Warehouse warehouseUpdate) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Товар с ID: " + id + " не найден"));

        warehouse.setCategory(warehouseUpdate.getCategory());
        warehouse.setProductName(warehouseUpdate.getProductName());
        warehouse.setWarehousePlace(warehouseUpdate.getWarehousePlace());
        warehouse.setProductPrice(warehouseUpdate.getProductPrice());
        warehouse.setQuantity(warehouseUpdate.getQuantity());
        return warehouseRepository.save(warehouse);
    }

    // DELETE
    public void deleteProduct(Long id) {
        warehouseRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Товар с ID: " + id + " не существует"));
        warehouseRepository.deleteById(id);
    }

    // MOVE FROM WAREHOUSE TO ANOTHER WAREHOUSE
    public Warehouse moveProductsFromTo(Long id, Integer fromPlace, Integer toPlace){
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Товар с ID: " + id + " не найден"));
        if (!warehouse.getWarehousePlace().equals(fromPlace)){
            throw new RuntimeException("Товар находится не на складе: " + fromPlace);
        }
        if (toPlace == 1 || toPlace == 2 || toPlace == 3){
            warehouse.setWarehousePlace(toPlace);
        } else {
            throw new RuntimeException("Некорректный целевой склад: " + toPlace);
        }
        return warehouseRepository.save(warehouse);
    }

    // UPDATE Increase Quantity
    public Warehouse getIncreaseQuantity(Long id, Integer increase){
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Товар с ID: " + id + " не найден"));
        if (increase < 0){
            throw new RuntimeException("Введенное число должно быть положительным");
        } else {
            warehouse.setQuantity(warehouse.getQuantity() + increase);
        }
        return warehouseRepository.save(warehouse);
    }

    //  UPDATE Decrease Quantity
    public Warehouse getDecreaseQuantity(Long id, Integer decrease) {
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Товар с ID: " + id + " не найден"));
        if (decrease < 0){
            throw new RuntimeException("Введенное число должно быть положительным");
        } else if (warehouse.getQuantity() < decrease){
            throw new RuntimeException("Количество товара недостаточно для списания " +
                    "\n Количество товара на складе: " + warehouse.getQuantity()
                    + " | Запрашиваемое количество к списанию: " + decrease);
        } else {
            warehouse.setQuantity(warehouse.getQuantity() - decrease);
        }
        return warehouseRepository.save(warehouse);
    }

    // SEARCH BY NAME
    public List<Warehouse> searchProductsByName(String name) {
        return warehouseRepository.findByProductNameContainingIgnoreCase(name);
    }

    // GET PRODUCTS OF NUMBER`S WAREHOUSE
    public List<Warehouse> searchProductsOfPlaceOfWarehouse(Integer warehousePlace) {
        return warehouseRepository.findByWarehousePlace(warehousePlace);
    }

    // GET PRODUCT WITH LOW STOCK
    public List<Warehouse> searchProductsWithLowStock() {
        List<Warehouse> lowStock = new ArrayList<>();
        List<Warehouse> allProducts = warehouseRepository.findAll();
        for (Warehouse warehouse : allProducts) {
            if (warehouse.getQuantity() < 1000) {
                lowStock.add(warehouse);
            }
        }
        return lowStock;
    }

    // GET COUNT PRODUCTS OF CATEGORY
    public List<CategoryStatsDto> getProductsOfCategories(String category) {
        return warehouseRepository.getCategoryStats(category);
    }

    // GET TOTAL SUM
    public String getTotalSum() {
        Double total = warehouseRepository.totalSumAllProducts();
        return String.format("%,.2f", total);
    }

    // GET PRODUCTS BETWEEN MIN & MAX PRICES
    public List<Warehouse> getProductsBetweenMinAndMaxPrices(Integer min, Integer max) {
        return warehouseRepository.findByProductPriceBetween(min, max);
    }

    // GET ALL SORT
    public List<Warehouse> getSortedAllProducts(String sortString, String order) {
        try{
        Sort.Direction direction = order.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortString);
        return warehouseRepository.findAll(sort);
        }catch(Exception e){
             throw new RuntimeException("Ошибка сортировки: " + e.getMessage());
        }
    }
}