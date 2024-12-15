package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.OrderDTO;
import com.example.clothingstore.dto.OrderItemDTO;
import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.entity.*;
import com.example.clothingstore.repository.CustomerRepository;
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

    @Autowired
    private CustomerRepository customerRepository;

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

    public Order addProductToUnconfirmedOrder(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new IllegalStateException("Not enough stock available");
        }
        Order order = orderRepository.findByCustomerAndStatus(customer, OrderStatus.UNCONFIRMED)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setCustomer(customer);
                    newOrder.setStatus(OrderStatus.UNCONFIRMED);
                    newOrder.setOrderDate(LocalDateTime.now());
                    newOrder.setTotal(0.0);
                    return orderRepository.save(newOrder);
                });

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getPrice());
        orderItemRepository.save(orderItem);
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        double newTotal = order.getTotal() + product.getPrice() * quantity;
        order.setTotal(newTotal);
        orderRepository.save(order);

        return order;
    }

    public List<OrderItemDTO> getOrderItemsByOrderId(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        return orderItems.stream().map(orderItem -> {
            OrderItemDTO dto = new OrderItemDTO();
            dto.setId(orderItem.getId());
            dto.setProductId(orderItem.getProduct() != null ? orderItem.getProduct().getId() : null);
            dto.setProductName(orderItem.getProduct() != null ? orderItem.getProduct().getName() : "Unknown");
            dto.setQuantity(orderItem.getQuantity());
            dto.setPrice(orderItem.getPrice());
            dto.setSubtotal(orderItem.getSubtotal() != null ? orderItem.getSubtotal() : 0.0); // Обработка null
            return dto;
        }).collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByCustomerAndStatus(Long customerId, OrderStatus status) {
        List<Order> orders = orderRepository.findByCustomerAndStatusOrderByOrderDateDesc(
                customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found")),
                status
        );
        return orders.stream()
                .map(order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setId(order.getId());
                    orderDTO.setTotal(order.getTotal());
                    orderDTO.setOrderDate(order.getOrderDate());
                    orderDTO.setStatus(order.getStatus());
                    return orderDTO;
                })
                .collect(Collectors.toList());
    }

    public void confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().equals(OrderStatus.UNCONFIRMED)) {
            throw new IllegalStateException("Only unconfirmed orders can be confirmed");
        }

        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
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
