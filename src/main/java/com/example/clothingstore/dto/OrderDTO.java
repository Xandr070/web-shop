package com.example.clothingstore.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long id;
    private String customerName;
    private LocalDateTime orderDate;
    private Double total;
    private List<String> productNames;
    private List<OrderItemDTO> orderItems;

    public OrderDTO(Long id, String customerName, LocalDateTime orderDate, Double total, List<String> productNames, List<OrderItemDTO> orderItems) {
        this.id = id;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.total = total;
        this.productNames = productNames;
        this.orderItems = orderItems;
    }

    public OrderDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<String> getProductNames() {
        return productNames;
    }

    public void setProductNames(List<String> productNames) {
        this.productNames = productNames;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
