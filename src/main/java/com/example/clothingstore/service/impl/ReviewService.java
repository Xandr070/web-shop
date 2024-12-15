package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.ReviewDTO;
import com.example.clothingstore.entity.*;
import com.example.clothingstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addReview(Long customerId, Long productId, int rating, String reviewText) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review();
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(rating);
        review.setReviewText(reviewText);

        reviewRepository.save(review);
    }

    public boolean checkIfReviewExists(Long customerId, Long productId) {
        List<Review> existingReviews = reviewRepository.findByCustomerIdAndProductId(customerId, productId);
        return !existingReviews.isEmpty();
    }

    public Map<String, List<ReviewDTO>> getReviewsGroupedByCategory(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        List<Product> products = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(OrderItem::getProduct)
                .distinct()
                .collect(Collectors.toList());

        List<Review> reviews = reviewRepository.findByProductIn(products);

        return reviews.stream()
                .collect(Collectors.groupingBy(
                        review -> review.getProduct().getCategory().getName(),
                        Collectors.mapping(this::convertToReviewDTO, Collectors.toList())
                ));
    }

    private ReviewDTO convertToReviewDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getProduct().getName(),
                review.getCustomer().getName(),
                review.getRating(),
                review.getReviewText(),
                review.getProduct().getCategory().getName()
        );
    }
}
