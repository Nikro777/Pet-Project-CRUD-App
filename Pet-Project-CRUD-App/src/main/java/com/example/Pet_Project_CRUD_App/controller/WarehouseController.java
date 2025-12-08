package com.example.Pet_Project_CRUD_App.controller;

import com.example.Pet_Project_CRUD_App.dto.CategoryStatsDto;
import com.example.Pet_Project_CRUD_App.dto.WarehouseRequestDto;
import com.example.Pet_Project_CRUD_App.dto.WarehouseResponseDto;
import com.example.Pet_Project_CRUD_App.entity.Warehouse;
import com.example.Pet_Project_CRUD_App.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    public List<WarehouseResponseDto> getAllProducts() {
        List<Warehouse> entities = warehouseService.getAllProducts();
        return warehouseService.toDtoList(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> getProductById(@PathVariable Long id) {
        Optional<Warehouse> product = warehouseService.getProductById(id);
        return product.map(w -> ResponseEntity.ok(
                warehouseService.toResponseDto(w))).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public List<WarehouseResponseDto> getProductsByCategory(@PathVariable String category) {
        List<Warehouse> entities = warehouseService.getProductsByCategory(category);
        return warehouseService.toDtoList(entities);
    }

    @GetMapping("/search")
    public List<WarehouseResponseDto> searchProducts(@RequestParam String name) {
        List<Warehouse> entities = warehouseService.searchProductsByName(name);
        return warehouseService.toDtoList(entities);
    }

    @GetMapping("/place/{warehousePlace}")
    public  List<WarehouseResponseDto> getProductsByWarehousePlace(@PathVariable Integer warehousePlace){
        List<Warehouse> entities = warehouseService.searchProductsOfPlaceOfWarehouse(warehousePlace);
        return warehouseService.toDtoList(entities);
    }

    @GetMapping("/quantityLowStock")
    public List<WarehouseResponseDto> getLowStockProducts(){
        List<Warehouse> entities = warehouseService.searchProductsWithLowStock();
        return warehouseService.toDtoList(entities);
    }

    @GetMapping("/countOf/{category}")
    public  List<CategoryStatsDto> getCountOfCategoryStats(@PathVariable String category){
        return warehouseService.getProductsOfCategories(category);
    }

    @GetMapping("/totalSum")
    public String sumAllProducts(){
        return warehouseService.getTotalSum();
    }

    @GetMapping("/filter")
    public List<WarehouseResponseDto> getProductsDiopazonPrice(@RequestParam Integer min, @RequestParam Integer max){
        List<Warehouse> entities = warehouseService.getProductsBetweenMinAndMaxPrices(min, max);
        return warehouseService.toDtoList(entities);
    }

    @GetMapping("/sorted")
    public List<WarehouseResponseDto> getSortedProducts(@RequestParam String sortString, @RequestParam(defaultValue = "asc") String order){
        List<Warehouse> entities = warehouseService.getSortedAllProducts(sortString, order);
        return warehouseService.toDtoList(entities);
    }

    @PostMapping
    public WarehouseResponseDto createProduct(@Valid @RequestBody WarehouseRequestDto requestDto) {
        Warehouse entity = warehouseService.createProductFromDto(requestDto);
        return warehouseService.toResponseDto(entity);
    }

    @PutMapping("/{id}")
    public WarehouseResponseDto  updateProduct(@PathVariable Long id, @Valid @RequestBody Warehouse warehouseUpdate){
        Warehouse entity = warehouseService.updateProduct(id, warehouseUpdate);
        return warehouseService.toResponseDto(entity);
    }

    @PutMapping("/{id}/move")
    public WarehouseResponseDto moveProductBetweenWarehouses(@PathVariable Long id,
                                                             @RequestParam Integer fromWarehouse, @RequestParam Integer toWarehouse) {
        Warehouse entity = warehouseService.moveProductsFromTo(id, fromWarehouse, toWarehouse);
        return warehouseService.toResponseDto(entity);
    }

    @PutMapping("/{id}/increase")
    public  WarehouseResponseDto  toIncreaseQuantity(@PathVariable Long id
            , @RequestParam Integer increase){
        Warehouse entity = warehouseService.getIncreaseQuantity(id, increase);
        return warehouseService.toResponseDto(entity);
    }

    @PutMapping("/{id}/decrease")
    public WarehouseResponseDto toDecreaseQuantity(@PathVariable Long id
    , @RequestParam Integer decrease){
        Warehouse entity =  warehouseService.getDecreaseQuantity(id, decrease);
        return warehouseService.toResponseDto(entity);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        warehouseService.deleteProduct(id);
    }
}