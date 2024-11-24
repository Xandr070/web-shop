package com.example.clothingstore.service;

import com.example.clothingstore.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {

    List<ReviewDTO> getReviewsByProductId(Long productId);

    boolean isCustomerEligibleForDiscount(Long customerId, Long categoryId);
}
