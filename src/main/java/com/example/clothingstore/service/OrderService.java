package com.example.clothingstore.service;

import com.example.clothingstore.dto.OrderDTO;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDTO> getAllOrders();

    Optional<OrderDTO> getOrderById(Long id);

    OrderDTO saveOrder(OrderDTO orderDTO);

    void deleteOrder(Long orderId);
}
