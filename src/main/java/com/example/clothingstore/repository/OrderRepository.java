package com.example.clothingstore.repository;

import com.example.clothingstore.entity.Customer;
import com.example.clothingstore.entity.Order;
import com.example.clothingstore.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByCustomerId(Long customerId);
    Optional<Order> findByCustomerAndStatus(Customer customer, OrderStatus status);
    List<Order> findByCustomerAndStatusOrderByOrderDateDesc(Customer customer, OrderStatus status);

}
