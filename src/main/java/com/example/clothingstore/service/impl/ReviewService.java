package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.ReviewDTO;
import com.example.clothingstore.entity.Review;
import com.example.clothingstore.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(review -> new ReviewDTO(
                        review.getId(),
                        review.getProduct().getName(),
                        review.getCustomer().getName(),
                        review.getRating(),
                        review.getReviewText()))
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByProduct(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return reviews.stream()
                .map(review -> new ReviewDTO(
                        review.getId(),
                        review.getProduct().getName(),
                        review.getCustomer().getName(),
                        review.getRating(),
                        review.getReviewText()))
                .collect(Collectors.toList());
    }

    public ReviewDTO createReview(Review review) {
        Review savedReview = reviewRepository.save(review);
        return new ReviewDTO(
                savedReview.getId(),
                savedReview.getProduct().getName(),
                savedReview.getCustomer().getName(),
                savedReview.getRating(),
                savedReview.getReviewText());
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
