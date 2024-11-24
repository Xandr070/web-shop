package com.example.clothingstore.service;

import com.example.clothingstore.dto.ReviewDTO;
import java.util.List;
import java.util.Optional;

public interface ReviewService {

    List<ReviewDTO> getAllReviews();

    Optional<ReviewDTO> getReviewById(Long id);

    ReviewDTO saveReview(ReviewDTO reviewDTO);

    void deleteReview(Long reviewId);

    List<ReviewDTO> getReviewsByProductId(Long productId);  // Добавляем метод
}
