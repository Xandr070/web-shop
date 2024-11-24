package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.ReviewDTO;
import com.example.clothingstore.entity.Review;
import com.example.clothingstore.repository.ReviewRepository;
import com.example.clothingstore.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isCustomerEligibleForDiscount(Long customerId, Long categoryId) {
        long reviewCount = reviewRepository.countByCustomerIdAndProductCategoryId(customerId, categoryId);
        return reviewCount >= 5;
    }
}
