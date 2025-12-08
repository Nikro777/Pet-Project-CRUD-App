package com.example.Pet_Project_CRUD_App.service;

import com.example.Pet_Project_CRUD_App.dto.*;
import com.example.Pet_Project_CRUD_App.entity.Category;
import com.example.Pet_Project_CRUD_App.entity.Product;
import com.example.Pet_Project_CRUD_App.entity.WarehousePlace;
import com.example.Pet_Project_CRUD_App.repository.CategoryRepository;
import com.example.Pet_Project_CRUD_App.repository.ProductRepository;
import com.example.Pet_Project_CRUD_App.repository.WarehousePlaceRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Data
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final WarehousePlaceRepository warehousePlaceRepository;

    // ========== ДЛЯ СПИСКА (МИНИМАЛЬНАЯ ИНФОРМАЦИЯ) ==========

    public List<ProductListDto> getAllProductsForList() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    private ProductListDto convertToListDto(Product product) {
        return new ProductListDto(
                product.getId(),
                product.getProductName(),
                product.getProductPrice(),
                product.getCategory().getName()
        );
    }

    // ========== ДЛЯ ДЕТАЛЬНОЙ ИНФОРМАЦИИ (ВСЕ ПОЛЯ) ==========

    public ProductDetailDto getProductDetailById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт с ID " + id + " не найден"));
        return convertToDetailDto(product);
    }

    private ProductDetailDto convertToDetailDto(Product product) {
        return new ProductDetailDto(
                product.getId(),
                product.getProductName(),
                product.getProductPrice(),
                product.getQuantity(),
                product.getCategory().getName(),
                product.getWarehousePlace().getPlaceNumber(),
                product.getProductPrice() * product.getQuantity()
        );
    }

    // Для места на складе (возвращает ProductLocationDto)
    public List<ProductLocationDto> getProductsByWarehousePlaceForLocation(Integer placeNumber) {
        return productRepository.findByWarehousePlace(placeNumber)
                .stream()
                .map(this::convertToLocationDto)
                .collect(Collectors.toList());
    }

    private ProductLocationDto convertToLocationDto(Product product) {
        return new ProductLocationDto(
                product.getId(),
                product.getProductName(),
                product.getCategory().getName(),
                product.getWarehousePlace().getPlaceNumber()
        );
    }

    // Для низких запасов (возвращает ProductStockDto)
    public List<ProductStockDto> getLowStockProductsForStock(Integer threshold) {
        return productRepository.findLowStockProducts(threshold)
                .stream()
                .map(this::convertToStockDto)
                .collect(Collectors.toList());
    }

    private ProductStockDto convertToStockDto(Product product) {
        return new ProductStockDto(
                product.getId(),
                product.getProductName(),
                product.getCategory().getName(),
                product.getQuantity()
        );
    }

    // ========== CRUD ОПЕРАЦИИ (ИСПОЛЬЗУЮТ ProductRequestDto) ==========

    public ProductDetailDto createProduct(ProductRequestDto dto) {
        // Проверяем уникальность названия
        if (productRepository.searchByProductName(dto.getProductName()).size() > 0) {
            throw new RuntimeException("Продукт с названием '" + dto.getProductName() + "' уже существует");
        }

        // Находим или создаем категорию
        Category category = categoryRepository.findByName(dto.getCategoryName());
        if (category == null) {
            category = new Category(dto.getCategoryName());
            categoryRepository.save(category);
        }

        // Находим место на складе
        WarehousePlace warehousePlace = warehousePlaceRepository.findByPlaceNumber(dto.getWarehousePlace());
        if (warehousePlace == null) {
            warehousePlace = new WarehousePlace(dto.getWarehousePlace());
            warehousePlaceRepository.save(warehousePlace);
        }

        // Создаем продукт
        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setProductPrice(dto.getProductPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategory(category);
        product.setWarehousePlace(warehousePlace);

        Product savedProduct = productRepository.save(product);
        return convertToDetailDto(savedProduct);
    }

    public ProductDetailDto updateProduct(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт с ID " + id + " не найден"));

        // Проверка уникальности при изменении названия
        if (!product.getProductName().equals(dto.getProductName()) &&
                productRepository.searchByProductName(dto.getProductName()).size() > 0) {
            throw new RuntimeException("Продукт с названием '" + dto.getProductName() + "' уже существует");
        }

        // Обновляем категорию если изменилась
        if (!product.getCategory().getName().equals(dto.getCategoryName())) {
            Category category = categoryRepository.findByName(dto.getCategoryName());
            if (category == null) {
                category = new Category(dto.getCategoryName());
                categoryRepository.save(category);
            }
            product.setCategory(category);
        }

        // Обновляем место если изменилось
        if (!product.getWarehousePlace().getPlaceNumber().equals(dto.getWarehousePlace())) {
            WarehousePlace warehousePlace = warehousePlaceRepository.findByPlaceNumber(dto.getWarehousePlace());
            if (warehousePlace == null) {
                warehousePlace = new WarehousePlace(dto.getWarehousePlace());
                warehousePlaceRepository.save(warehousePlace);
            }
            product.setWarehousePlace(warehousePlace);
        }

        // Обновляем остальные поля
        product.setProductName(dto.getProductName());
        product.setProductPrice(dto.getProductPrice());
        product.setQuantity(dto.getQuantity());

        Product updatedProduct = productRepository.save(product);
        return convertToDetailDto(updatedProduct);
    }

    public List<ProductListDto> getProductsByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName)
                .stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    public List<ProductListDto> searchProductsByName(String name) {
        return productRepository.searchByProductName(name)
                .stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    public List<ProductListDto> getProductsByWarehousePlace(Integer placeNumber) {
        return productRepository.findByWarehousePlace(placeNumber)
                .stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    public List<ProductListDto> getLowStockProducts(Integer threshold) {
        return productRepository.findLowStockProducts(threshold)
                .stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    public List<ProductListDto> getProductsByPriceRange(Double min, Double max) {
        return productRepository.findByPriceBetween(min, max)
                .stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Продукт с ID " + id + " не найден");
        }
        productRepository.deleteById(id);
    }
}