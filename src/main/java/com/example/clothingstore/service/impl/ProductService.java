package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.entity.Product;
import com.example.clothingstore.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        productDTO.setCategoryName(product.getCategory().getName());
        productDTO.setCategoryId(product.getCategory().getId()); // Установите categoryId
        return productDTO;
    }



    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> filterProductsByPrice(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(this::convertToDTO);
    }

    public ProductDTO addProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long productId, Product updatedProduct) {
        if (productRepository.existsById(productId)) {
            updatedProduct.setId(productId);
            Product savedProduct = productRepository.save(updatedProduct);
            return convertToDTO(savedProduct);
        }
        return null;
    }

    public List<ProductDTO> getProductsByCategoryName(String categoryName) {
        return productRepository.findByCategoryName(categoryName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public Optional<ProductDTO> getProductByName(String productName) {
        return productRepository.findByName(productName)
                .map(this::convertToDTO);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
