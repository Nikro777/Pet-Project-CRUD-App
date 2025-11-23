package com.example.Pet_Project_CRUD_App.controller;

import com.example.Pet_Project_CRUD_App.dto.CategoryStatsDto;
import com.example.Pet_Project_CRUD_App.entity.Warehouse;
import com.example.Pet_Project_CRUD_App.repository.WarehouseRepository;
import com.example.Pet_Project_CRUD_App.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.*;


@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    // Тестовый endpoint для диагностики
    @GetMapping("/test")
    public String testConnection() {
        try {
            long count = warehouseRepository.count();
            return "Total records in database: " + count;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    // добавим диагностику

    @GetMapping("/full-debug")
    public String fullDebug() {
        try {
            // 1. Проверка прямого SQL
            Integer sqlCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM public.warehouses_table", Integer.class);

            // 2. Проверка через JPA
            long jpaCount = warehouseRepository.count();

            // 3. Проверка первых записей
            List<Map<String, Object>> firstRecords = jdbcTemplate.queryForList(
                    "SELECT * FROM public.warehouses_table LIMIT 2");

            // 4. Проверка структуры Entity
            List<Warehouse> jpaRecords = warehouseRepository.findAll();

            return String.format(
                    "SQL Count: %d, JPA Count: %d, First SQL records: %s, First JPA records: %d",
                    sqlCount, jpaCount, firstRecords.toString(), jpaRecords.size()
            );
        } catch (Exception e) {
            return "Debug Error: " + e.getMessage();
        }
    }




    // GET ALL PRODUCTS
    @GetMapping
    public List<Warehouse> getAllProducts() {
        return warehouseService.getAllProducts();
    }

    // GET PRODUCT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getProductById(@PathVariable Long id) {
        Optional<Warehouse> product = warehouseService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET PRODUCTS BY CATEGORY
    @GetMapping("/category/{category}")
    public List<Warehouse> getProductsByCategory(@PathVariable String category) {
        return warehouseService.getProductsByCategory(category);
    }

    // GET PRODUCTS BY NAME
    @GetMapping("/search")
    public List<Warehouse> searchProducts(@RequestParam String name) {
        return warehouseService.searchProductsByName(name);
    }

    // GET PRODUCTS OF NUMBER`S WAREHOUSE
    @GetMapping("/place/{warehousePlace}")
    public  List<Warehouse> getProductsByWarehousePlace(@PathVariable Integer warehousePlace){
        return warehouseService.searchProductsOfPlaceOfWarehouse(warehousePlace);
    }

    // GET PRODUCT WITH LOW STOCK
    @GetMapping("/quantityLowStock")
    public List<Warehouse> getLowStockProducts(){
        return warehouseService.searchProductsWithLowStock();
    }

    // GET STATS COUNT PRODUCT OF CATEGORY
    @GetMapping("/countOf/{category}")
    public  List<CategoryStatsDto> getCountOfCategoryStats(@PathVariable String category){
        return warehouseService.getProductsOfCategories(category);
    }

    // GET TOTAL SUM PRODUCTS
    @GetMapping("/totalSum")
    public String sumAllProducts(){
        return warehouseService.getTotalSum();
    }

    // GET PRODUCTS BETWEEN TWO PRICES /localhost:8080/api/warehouse/filter?min=200&max=500
    @GetMapping("/filter")
    public List<Warehouse> getProductsDiopazonPrice(@RequestParam Integer min, @RequestParam Integer max){
        return warehouseService.getProductsBetweenMinAndMaxPrices(min, max);
    }

    // GET ALL PRODUCT SORT /localhost:8080/api/warehouse/sorted?sortString=productPrice&order=desc
    @GetMapping("/sorted")
    public List<Warehouse> getSortedProducts(@RequestParam String sortString, @RequestParam(defaultValue = "asc") String order){
        return warehouseService.getSortedAllProducts(sortString, order);
    }

    // CREATE NEW PRODUCT
    @PostMapping
    public Warehouse createProduct(@RequestBody Warehouse warehouse) {
        return warehouseService.createProduct(warehouse);
    }

    // UPDATE PRODUCT
    @PutMapping("/{id}")
    public Warehouse updateProduct(@PathVariable Long id, @RequestBody Warehouse warehouseUpdate){
        return warehouseService.updateProduct(id, warehouseUpdate);
    }

    // MOVE PRODUCT BETWEEN WAREHOUSES
    @PutMapping("/{id}/move")
    public Warehouse moveProductBetweenWarehouses(@PathVariable Long id
            , @RequestParam Integer fromWarehouse, @RequestParam Integer toWarehouse){
        return warehouseService.moveProductsFromTo(id, fromWarehouse, toWarehouse);
    }

    // UPDATE Increase Quantity
    @PutMapping("/{id}/increase")
    public  Warehouse toIncreaseQuantity(@PathVariable Long id
            , @RequestParam Integer increase){
        return warehouseService.getIncreaseQuantity(id, increase);
    }

    // UPDATE Decrease Quantity
    @PutMapping("/{id}/decrease")
    public Warehouse toDecreaseQuantity(@PathVariable Long id
    , @RequestParam Integer decrease){
        return warehouseService.getDecreaseQuantity(id, decrease);
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        warehouseService.deleteProduct(id);
    }
}
