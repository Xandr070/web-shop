package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.ReviewDTO;
import com.example.clothingstore.entity.Order;
import com.example.clothingstore.entity.OrderItem;
import com.example.clothingstore.entity.Product;
import com.example.clothingstore.entity.Review;
import com.example.clothingstore.repository.OrderRepository;
import com.example.clothingstore.repository.ReviewRepository;
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

    public Map<String, List<ReviewDTO>> getReviewsGroupedByCategory(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        List<Product> products = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(OrderItem::getProduct)
                .distinct()
                .collect(Collectors.toList());

        System.out.println("Products for customer ID " + customerId + ": " + products);

        List<Review> reviews = reviewRepository.findByProductIn(products);

        System.out.println("Reviews for customer ID " + customerId + ": " + reviews);

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
