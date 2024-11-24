package com.example.clothingstore.service;

import com.example.clothingstore.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getOrdersByCustomerId(Long customerId);

    OrderDTO createOrder(OrderDTO orderDTO);

    double calculateTotalSalesForSeason(String season);
}
