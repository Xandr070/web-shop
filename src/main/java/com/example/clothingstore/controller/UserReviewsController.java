package com.example.clothingstore.controller;

import com.example.clothingstore.service.impl.CustomerDetailsService;
import com.example.clothingstore.service.impl.ReviewService;
import com.example.clothingstore.service.impl.ProductService;
import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserReviewsController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @GetMapping("/user-reviews")
    public String showUserReviewsPage(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long customerId = customerDetailsService.getCustomerIdByEmail(user.getUsername());

        if (customerId == null) {
            return "redirect:/store";
        }

        Map<String, List<ReviewDTO>> reviewsByCategory = reviewService.getReviewsGroupedByCategory(customerId);
        Map<String, List<ProductDTO>> productsByCategory = new HashMap<>();
        Map<String, Boolean> categoryDiscountEligibility = new HashMap<>();

        reviewsByCategory.forEach((categoryName, reviews) -> {
            boolean isEligibleForDiscount = reviews.size() >= 5;
            categoryDiscountEligibility.put(categoryName, isEligibleForDiscount);
            List<ProductDTO> products = productService.getProductsByCategoryName(categoryName);
            if (isEligibleForDiscount) {
                products.forEach(product -> product.setPrice(product.getPrice() * 0.9));
            }
            productsByCategory.put(categoryName, products);
        });

        model.addAttribute("reviewsByCategory", reviewsByCategory);
        model.addAttribute("productsByCategory", productsByCategory);
        model.addAttribute("categoryDiscountEligibility", categoryDiscountEligibility);

        return "UserReviews";
    }
}
