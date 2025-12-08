package com.example.Pet_Project_CRUD_App.controller;

import com.example.Pet_Project_CRUD_App.dto.*;
import com.example.Pet_Project_CRUD_App.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ========== ДЛЯ СПИСКА (РАЗНЫЕ DTO ДЛЯ РАЗНЫХ ЦЕЛЕЙ) ==========

    @GetMapping("/list")
    public List<ProductListDto> getProductsList() {
        return productService.getAllProductsForList();
    }

    @GetMapping("/category/{categoryName}")
    public List<ProductListDto> getProductsByCategory(@PathVariable String categoryName) {
        return productService.getProductsByCategory(categoryName);
    }

    @GetMapping("/search")
    public List<ProductListDto> searchProducts(@RequestParam String name) {
        return productService.searchProductsByName(name);
    }

    @GetMapping("/place/{placeNumber}")
    public List<ProductLocationDto> getProductsByWarehousePlace(@PathVariable Integer placeNumber) {
        return productService.getProductsByWarehousePlaceForLocation(placeNumber);
    }

    @GetMapping("/low-stock")
    public List<ProductStockDto> getLowStockProducts(
            @RequestParam(defaultValue = "1000") Integer threshold) {
        return productService.getLowStockProductsForStock(threshold);
    }

    @GetMapping("/filter/price")
    public List<ProductListDto> filterByPrice(
            @RequestParam Double min,
            @RequestParam Double max) {
        return productService.getProductsByPriceRange(min, max);
    }

    // ========== ДЛЯ ДЕТАЛЬНОЙ ИНФОРМАЦИИ ==========

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDto> getProductDetail(@PathVariable Long id) {
        try {
            ProductDetailDto product = productService.getProductDetailById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ProductDetailDto createProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        return productService.createProduct(requestDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto requestDto) {
        try {
            ProductDetailDto updatedProduct = productService.updateProduct(id, requestDto);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========== ОПЕРАЦИИ СО СКЛАДОМ ==========

    @PutMapping("/{id}/move")
    public ResponseEntity<ProductDetailDto> moveProduct(
            @PathVariable Long id,
            @RequestParam Integer newPlace) {
        try {
            ProductDetailDto product = productService.getProductDetailById(id);

            ProductRequestDto updateDto = new ProductRequestDto();
            updateDto.setProductName(product.getProductName());
            updateDto.setProductPrice(product.getProductPrice());
            updateDto.setQuantity(product.getQuantity());
            updateDto.setCategoryName(product.getCategoryName());
            updateDto.setWarehousePlace(newPlace);

            ProductDetailDto updatedProduct = productService.updateProduct(id, updateDto);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/increase")
    public ResponseEntity<ProductDetailDto> increaseQuantity(
            @PathVariable Long id,
            @RequestParam Integer amount) {
        try {
            ProductDetailDto product = productService.getProductDetailById(id);

            ProductRequestDto updateDto = new ProductRequestDto();
            updateDto.setProductName(product.getProductName());
            updateDto.setProductPrice(product.getProductPrice());
            updateDto.setQuantity(product.getQuantity() + amount);
            updateDto.setCategoryName(product.getCategoryName());
            updateDto.setWarehousePlace(product.getWarehousePlace());

            ProductDetailDto updatedProduct = productService.updateProduct(id, updateDto);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/decrease")
    public ResponseEntity<ProductDetailDto> decreaseQuantity(
            @PathVariable Long id,
            @RequestParam Integer amount) {
        try {
            ProductDetailDto product = productService.getProductDetailById(id);

            int newQuantity = product.getQuantity() - amount;
            if (newQuantity < 0) {
                throw new RuntimeException("Недостаточно товара для списания");
            }

            ProductRequestDto updateDto = new ProductRequestDto();
            updateDto.setProductName(product.getProductName());
            updateDto.setProductPrice(product.getProductPrice());
            updateDto.setQuantity(newQuantity);
            updateDto.setCategoryName(product.getCategoryName());
            updateDto.setWarehousePlace(product.getWarehousePlace());

            ProductDetailDto updatedProduct = productService.updateProduct(id, updateDto);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}