package com.example.clothingstore.service;

import com.example.clothingstore.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    Optional<ProductDTO> getProductById(Long id);

    ProductDTO saveProduct(ProductDTO productDTO);

    void deleteProduct(Long productId);

    List<ProductDTO> getTopProductsBySalesSeason(String season);
}
