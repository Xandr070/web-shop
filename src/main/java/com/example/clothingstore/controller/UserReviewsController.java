package com.example.clothingstore.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.clothingstore.service.impl.ReviewService;
import com.example.clothingstore.service.impl.ProductService;
import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.dto.ReviewDTO;
import com.example.clothingstore_contracts.controller.UserReviewsControllerContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserReviewsController implements UserReviewsControllerContract {

    private static final Logger logger = LoggerFactory.getLogger(UserReviewsController.class);

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @GetMapping("/user-reviews")
    public String showUserReviewsPage(@RequestParam Long customerId, Model model) {
        if (customerId == null) {
            logger.warn("Попытка просмотра отзывов без указания идентификатора клиента.");
            return "redirect:/store";
        }

        logger.info("Просмотр отзывов пользователя с ID={}.", customerId);

        Map<String, List<ReviewDTO>> reviewsByCategory = reviewService.getReviewsGroupedByCategory(customerId);
        logger.debug("Отзывы, сгруппированные по категориям: {}", reviewsByCategory);

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

        logger.info("Отзывы и продукты успешно загружены для пользователя с ID={}.", customerId);
        return "UserReviews";
    }
}
