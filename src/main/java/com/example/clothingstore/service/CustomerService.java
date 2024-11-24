package com.example.clothingstore.service;

import com.example.clothingstore.dto.CustomerDTO;

import java.util.Optional;

public interface CustomerService {

    Optional<CustomerDTO> getCustomerById(Long customerId);

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    boolean isEligibleForCategoryDiscount(Long customerId, Long categoryId);

    void deleteCustomerIfNoOrders(Long customerId);
}
