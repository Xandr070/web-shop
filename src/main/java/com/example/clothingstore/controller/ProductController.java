package com.example.clothingstore.controller;

import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.entity.Product;
import com.example.clothingstore.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductDTO> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("/filter")
    public List<ProductDTO> filterProductsByPrice(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        return productService.filterProductsByPrice(minPrice, maxPrice);
    }

    @GetMapping("/{productId}")
    public Optional<ProductDTO> getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    public ProductDTO addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PutMapping("/{productId}")
    public ProductDTO updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        return productService.updateProduct(productId, updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }
}
