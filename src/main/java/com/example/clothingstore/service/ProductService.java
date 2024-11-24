package com.example.clothingstore.service;

import com.example.clothingstore.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getProductsByCategory(Long categoryId);

    List<ProductDTO> getTopProductsBySeason(String season);
}
