package com.example.clothingstore.controller;

import com.example.clothingstore.dto.OrderDTO;
import com.example.clothingstore.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderDTO> getOrdersByCustomer(@PathVariable Long customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    @GetMapping("/{orderId}")
    public Optional<OrderDTO> getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PostMapping
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO);
    }

    @PutMapping("/{orderId}")
    public OrderDTO updateOrder(@PathVariable Long orderId, @RequestBody OrderDTO updatedOrderDTO) {
        return orderService.updateOrder(orderId, updatedOrderDTO);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }
}
