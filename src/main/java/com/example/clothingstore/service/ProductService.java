package com.example.clothingstore.service;

import com.example.clothingstore.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product saveProduct(Product product);
    void deleteProduct(Long productId);
    List<Product> getTopProductsBySalesSeason(String season);
}
