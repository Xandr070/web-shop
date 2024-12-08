package com.example.clothingstore.repository;

import com.example.clothingstore.entity.Review;
import com.example.clothingstore.entity.Customer;
import com.example.clothingstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);

    long countByCustomerAndProductCategory(Customer customer, Category category);
}
