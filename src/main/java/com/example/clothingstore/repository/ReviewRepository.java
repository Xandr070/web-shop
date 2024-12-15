package com.example.clothingstore.repository;

import com.example.clothingstore.entity.Customer;
import com.example.clothingstore.entity.Review;
import com.example.clothingstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductIn(List<Product> products);

    List<Review> findByProduct(Product product);

    List<Review> findByProductId(Long productId);
    List<Review> findByCustomerIdAndProductId(Long customerId, Long productId);
    boolean existsByCustomerAndProduct(Customer customer, Product product);

}
