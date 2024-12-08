package com.example.clothingstore.controller;

import com.example.clothingstore.dto.ReviewDTO;
import com.example.clothingstore.entity.Customer;
import com.example.clothingstore.entity.Product;
import com.example.clothingstore.entity.Review;
import com.example.clothingstore.service.impl.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public List<ReviewDTO> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/product/{productId}")
    public List<ReviewDTO> getReviewsByProduct(@PathVariable Long productId) {
        return reviewService.getReviewsByProduct(productId);
    }

    @PostMapping
    public ReviewDTO createReview(@RequestBody ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setProduct(new Product());
        review.setCustomer(new Customer());
        review.setRating(reviewDTO.getRating());
        review.setReviewText(reviewDTO.getReviewText());

        return reviewService.createReview(review);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
