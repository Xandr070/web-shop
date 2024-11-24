package com.example.clothingstore.service;

import com.example.clothingstore.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();

    Optional<CustomerDTO> getCustomerById(Long id);

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    double calculateCustomerAverageOrderValue(Long customerId);

    long countCustomerOrders(Long customerId);

    boolean eligibleForCategoryDiscount(Long customerId, Long categoryId);
}
