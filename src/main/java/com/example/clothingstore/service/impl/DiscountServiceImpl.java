package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.entity.Category;
import com.example.clothingstore.entity.Customer;
import com.example.clothingstore.repository.CategoryRepository;
import com.example.clothingstore.repository.CustomerRepository;
import com.example.clothingstore.repository.ReviewRepository;
import com.example.clothingstore.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final CustomerRepository customerRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public DiscountServiceImpl(CustomerRepository customerRepository,
                               CategoryRepository categoryRepository,
                               ReviewRepository reviewRepository) {
        this.customerRepository = customerRepository;
        this.categoryRepository = categoryRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<ProductDTO> calculateDiscount(Long customerId, Long categoryId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        Category category = categoryRepository.findById(categoryId).orElseThrow();

        long reviewCount = reviewRepository.countByCustomerAndProductCategory(customer, category);

        if (reviewCount >= 5) {
            return category.getProducts().stream()
                    .map(product -> new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getStock(), category.getName()))
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }
}
