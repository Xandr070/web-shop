package com.example.clothingstore.repository;

import com.example.clothingstore.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);

    long countByCustomerIdAndProductCategoryId(Long customerId, Long categoryId);
}
