package com.example.clothingstore.repository;

import com.example.clothingstore.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);

    @Query("SELECT COUNT(r) FROM Review r JOIN Product p ON r.product.id = p.id " +
            "WHERE r.customer.id = :customerId AND p.category.id = :categoryId")
    long countByCustomerIdAndProductCategoryId(@Param("customerId") Long customerId, @Param("categoryId") Long categoryId);
}
