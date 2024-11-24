package com.example.clothingstore.service;

import com.example.clothingstore.entity.Customer;

import java.util.Optional;

public interface CustomerService {
    Optional<Customer> getCustomerById(Long id);
    Customer saveCustomer(Customer customer);
    void deleteCustomer(Long customerId);
    double calculateCustomerAverageOrderValue(Long customerId);
    long countCustomerOrders(Long customerId);
    boolean eligibleForCategoryDiscount(Long customerId, Long categoryId);
}
