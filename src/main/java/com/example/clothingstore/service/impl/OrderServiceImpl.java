package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.OrderDTO;
import com.example.clothingstore.entity.Order;
import com.example.clothingstore.repository.OrderRepository;
import com.example.clothingstore.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(modelMapper.map(order, OrderDTO.class));
        }
        return orderDTOs;
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    @Override
    public double calculateTotalSalesForSeason(String season) {
        List<Integer> seasonMonths = getSeasonMonths(season);
        return orderRepository.calculateTotalSalesByMonths(seasonMonths);
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
