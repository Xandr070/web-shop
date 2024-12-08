package com.example.clothingstore.repository;

import com.example.clothingstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
}
