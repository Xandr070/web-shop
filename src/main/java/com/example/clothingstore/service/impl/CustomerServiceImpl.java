package com.example.clothingstore.service.impl;

import com.example.clothingstore.entity.Customer;
import com.example.clothingstore.entity.Order;
import com.example.clothingstore.repository.CustomerRepository;
import com.example.clothingstore.repository.OrderRepository;
import com.example.clothingstore.repository.ReviewRepository;
import com.example.clothingstore.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, OrderRepository orderRepository, ReviewRepository reviewRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        if (orderRepository.findByCustomerId(customerId).isEmpty()) {
            customerRepository.deleteById(customerId);
        } else {
            throw new IllegalStateException("Нельзя удалить клиента с активными заказами.");
        }
    }

    @Override
    public double calculateCustomerAverageOrderValue(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        double totalSum = 0.0;
        int orderCount = 0;

        for (Order order : orders) {
            totalSum += order.getTotal();
            orderCount++;
        }
        return orderCount > 0 ? totalSum / orderCount : 0.0;
    }


    @Override
    public long countCustomerOrders(Long customerId) {
        return orderRepository.findByCustomerId(customerId).size();
    }

    @Override
    public boolean eligibleForCategoryDiscount(Long customerId, Long categoryId) {
        long reviewCount = reviewRepository.countByCustomerIdAndProductCategoryId(customerId, categoryId);
        return reviewCount >= 5; // 5 - пороговое значение для скидки, можно вынести в конфигурацию
    }
}
