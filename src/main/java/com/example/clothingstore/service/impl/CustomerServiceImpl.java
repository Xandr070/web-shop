package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.CustomerDTO;
import com.example.clothingstore.entity.Customer;
import com.example.clothingstore.repository.CustomerRepository;
import com.example.clothingstore.repository.OrderRepository;
import com.example.clothingstore.repository.ReviewRepository;
import com.example.clothingstore.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, OrderRepository orderRepository, ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class));
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        Customer savedCustomer = customerRepository.save(customer);
        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

    @Override
    public boolean isEligibleForCategoryDiscount(Long customerId, Long categoryId) {
        long reviewCount = reviewRepository.countByCustomerIdAndProductCategoryId(customerId, categoryId);
        return reviewCount >= 5;
    }

    @Override
    public void deleteCustomerIfNoOrders(Long customerId) {
        if (orderRepository.findByCustomerId(customerId).isEmpty()) {
            customerRepository.deleteById(customerId);
        } else {
            throw new IllegalStateException("Нельзя удалить клиента с активными заказами.");
        }
    }
}
