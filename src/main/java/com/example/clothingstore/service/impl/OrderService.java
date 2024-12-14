package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.entity.Order;
import com.example.clothingstore.entity.OrderItem;
import com.example.clothingstore.entity.Product;
import com.example.clothingstore.repository.OrderRepository;
import com.example.clothingstore.repository.OrderItemRepository;
import com.example.clothingstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<ProductDTO> getPopularProductsForSeason(String season) {
        LocalDateTime seasonStart = getSeasonStart(season);
        LocalDateTime seasonEnd = seasonStart.plusMonths(3);

        List<Order> orders = orderRepository.findByOrderDateBetween(seasonStart, seasonEnd);

        Map<Long, Integer> productQuantities = new HashMap<>();
        for (Order order : orders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                productQuantities.put(orderItem.getProduct().getId(),
                        productQuantities.getOrDefault(orderItem.getProduct().getId(), 0) + orderItem.getQuantity());
            }
        }

        List<ProductDTO> popularProducts = productQuantities.entrySet().stream()
                .map(entry -> {
                    Product product = productRepository.findById(entry.getKey()).orElse(null);
                    if (product != null) {
                        ProductDTO productDTO = new ProductDTO(product.getId(), product.getName(), product.getPrice(),
                                product.getStock(), product.getCategory().getName(), product.getCategory().getId());
                        productDTO.setSoldQuantity(entry.getValue());
                        return productDTO;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(ProductDTO::getSoldQuantity).reversed())
                .collect(Collectors.toList());

        return popularProducts;
    }

    private LocalDateTime getSeasonStart(String season) {
        int year = LocalDateTime.now().getYear();
        return switch (season) {
            case "Spring" -> LocalDateTime.of(year, Month.MARCH, 1, 0, 0, 0, 0);
            case "Summer" -> LocalDateTime.of(year, Month.JUNE, 1, 0, 0, 0, 0);
            case "Autumn" -> LocalDateTime.of(year, Month.SEPTEMBER, 1, 0, 0, 0, 0);
            case "Winter" -> LocalDateTime.of(year, Month.DECEMBER, 1, 0, 0, 0, 0);
            default -> throw new IllegalArgumentException("Unknown season: " + season);
        };
    }
}
