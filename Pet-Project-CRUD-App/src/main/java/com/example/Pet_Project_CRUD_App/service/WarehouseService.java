package com.example.Pet_Project_CRUD_App.service;

import com.example.Pet_Project_CRUD_App.dto.CategoryStatsDto;
import com.example.Pet_Project_CRUD_App.dto.WarehouseRequestDto;
import com.example.Pet_Project_CRUD_App.dto.WarehouseResponseDto;
import com.example.Pet_Project_CRUD_App.entity.Warehouse;
import com.example.Pet_Project_CRUD_App.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseResponseDto toResponseDto(Warehouse warehouse) {
        WarehouseResponseDto dto = new WarehouseResponseDto();
        dto.setId(warehouse.getId());
        dto.setCategory(warehouse.getCategory());
        dto.setProductName(warehouse.getProductName());
        dto.setProductPrice(warehouse.getProductPrice());
        return dto;
    }
    public List<WarehouseResponseDto> toDtoList(List<Warehouse> warehouses) {
        List<WarehouseResponseDto> dtos = new ArrayList<>();
        for (Warehouse warehouse : warehouses) {
            dtos.add(toResponseDto(warehouse));
        }
        return dtos;
    }

    public List<Warehouse> getAllProducts() {
        return warehouseRepository.findAll();
    }

    public Optional<Warehouse> getProductById(Long id) {
        return warehouseRepository.findById(id);
    }

    public List<Warehouse> getProductsByCategory(String category) {
        return warehouseRepository.findByCategory(category);
    }

    public Warehouse createProductFromDto(WarehouseRequestDto requestDto) {
        if (warehouseRepository.existsByProductNameIgnoreCase(requestDto.getProductName())) {
            throw new RuntimeException("Товар с таким названием уже существует: "
                    + requestDto.getProductName());
        }
        Warehouse entity = new Warehouse();
        entity.setCategory(requestDto.getCategory());
        entity.setProductName(requestDto.getProductName());
        entity.setWarehousePlace(requestDto.getWarehousePlace());
        entity.setProductPrice(requestDto.getProductPrice());
        entity.setQuantity(requestDto.getQuantity());

        return warehouseRepository.save(entity);
    }

    public Warehouse updateProduct(Long id, Warehouse warehouseUpdate) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Товар с ID: " + id + " не найден"));

        if (!warehouse.getProductName().equalsIgnoreCase(warehouseUpdate.getProductName()) &&
                warehouseRepository.existsByProductNameIgnoreCase(warehouseUpdate.getProductName())) {
            throw new RuntimeException("Товар с таким названием уже существует: " + warehouseUpdate.getProductName());
        }

        warehouse.setCategory(warehouseUpdate.getCategory());
        warehouse.setProductName(warehouseUpdate.getProductName());
        warehouse.setWarehousePlace(warehouseUpdate.getWarehousePlace());
        warehouse.setProductPrice(warehouseUpdate.getProductPrice());
        warehouse.setQuantity(warehouseUpdate.getQuantity());
        return warehouseRepository.save(warehouse);
    }

    public void deleteProduct(Long id) {
        warehouseRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Товар с ID: " + id + " не существует"));
        warehouseRepository.deleteById(id);
    }

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

    public List<Warehouse> searchProductsByName(String name) {
        return warehouseRepository.findByProductNameContainingIgnoreCase(name);
    }

    public List<Warehouse> searchProductsOfPlaceOfWarehouse(Integer warehousePlace) {
        return warehouseRepository.findByWarehousePlace(warehousePlace);
    }

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

    public List<CategoryStatsDto> getProductsOfCategories(String category) {
        return warehouseRepository.getCategoryStats(category);
    }

    public String getTotalSum() {
        Double total = warehouseRepository.totalSumAllProducts();
        return String.format("%,.2f", total);
    }

    public List<Warehouse> getProductsBetweenMinAndMaxPrices(Integer min, Integer max) {
        return warehouseRepository.findByProductPriceBetween(min, max);
    }

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