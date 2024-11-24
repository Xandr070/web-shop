package com.example.clothingstore.service.impl;

import com.example.clothingstore.entity.OrderItem;
import com.example.clothingstore.entity.Product;
import com.example.clothingstore.repository.OrderItemRepository;
import com.example.clothingstore.repository.ProductRepository;
import com.example.clothingstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        List<OrderItem> associatedOrderItems = orderItemRepository.findByProductId(productId);
        if (!associatedOrderItems.isEmpty()) {
            throw new IllegalStateException("Нельзя удалить продукт, так как он связан с существующими заказами.");
        }
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> getTopProductsBySalesSeason(String season) {
        List<Integer> seasonMonths = getSeasonMonths(season);
        List<Object[]> topProductsData = orderItemRepository.findTopProductsBySeason(seasonMonths);

        List<Product> topProducts = new ArrayList<>();

        for (Object[] data : topProductsData) {
            if (data[0] instanceof Product) {
                Product product = (Product) data[0];
                topProducts.add(product);
            }
        }

        return topProducts;
    }

    private List<Integer> getSeasonMonths(String season) {
        switch (season.toLowerCase()) {
            case "зима":
                return List.of(12, 1, 2);
            case "весна":
                return List.of(3, 4, 5);
            case "лето":
                return List.of(6, 7, 8);
            case "осень":
                return List.of(9, 10, 11);
            default:
                throw new IllegalArgumentException("Неверный сезон: " + season);
        }
    }
}
