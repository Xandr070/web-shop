package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.OrderDTO;
import com.example.clothingstore.dto.OrderItemDTO;
import com.example.clothingstore.entity.Order;
import com.example.clothingstore.entity.OrderItem;
import com.example.clothingstore.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    private OrderItemDTO convertToOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setProductName(orderItem.getProduct().getName());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setPrice(orderItem.getPrice());
        orderItemDTO.setSubtotal(orderItem.getQuantity() * orderItem.getPrice());
        return orderItemDTO;
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

        orderDTO.setCustomerName(order.getCustomer().getName());
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(this::convertToOrderItemDTO)
                .collect(Collectors.toList());
        orderDTO.setOrderItems(orderItemDTOs);

        List<String> productNames = order.getOrderItems().stream()
                .map(orderItem -> orderItem.getProduct().getName())
                .collect(Collectors.toList());
        orderDTO.setProductNames(productNames);

        return orderDTO;
    }

    private Order convertToEntity(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderDTO> getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDTO);
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    public OrderDTO updateOrder(Long orderId, OrderDTO updatedOrderDTO) {
        if (orderRepository.existsById(orderId)) {
            Order updatedOrder = convertToEntity(updatedOrderDTO);
            updatedOrder.setId(orderId);
            Order savedOrder = orderRepository.save(updatedOrder);
            return convertToDTO(savedOrder);
        }
        return null;
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
