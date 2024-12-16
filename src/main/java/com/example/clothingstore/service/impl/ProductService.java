package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.entity.Category;
import com.example.clothingstore.entity.Product;
import com.example.clothingstore.repository.CategoryRepository;
import com.example.clothingstore.repository.OrderItemRepository;
import com.example.clothingstore.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        if (product.getCategory() != null) {
            productDTO.setCategoryName(product.getCategory().getName());
            productDTO.setCategoryId(product.getCategory().getId());
        } else {
            productDTO.setCategoryName("No Category");
            productDTO.setCategoryId(null);
        }

        return productDTO;
    }


    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "products", key = "#categoryId")
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

    public void addProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setCategory(category);

        product.setCreatedAt(LocalDateTime.now());

        productRepository.save(product);
    }


    public ProductDTO updateProduct(Long productId, ProductDTO updatedProductDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(updatedProductDTO.getName());
        existingProduct.setPrice(updatedProductDTO.getPrice());
        existingProduct.setStock(updatedProductDTO.getStock());

        Category category = categoryRepository.findById(updatedProductDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);

        return convertToDTO(updatedProduct);
    }

    @Cacheable(value = "products", key = "#categoryName")
    public List<ProductDTO> getProductsByCategoryName(String categoryName) {
        return productRepository.findByCategoryName(categoryName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProduct(Long productId) {
        orderItemRepository.deleteByProductId(productId);
        productRepository.deleteById(productId);
    }


}
