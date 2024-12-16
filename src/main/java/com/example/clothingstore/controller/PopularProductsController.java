package com.example.clothingstore.controller;

import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.service.impl.OrderService;
import com.example.clothingstore_contracts.controller.PopularProductsControllerContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PopularProductsController implements PopularProductsControllerContract {

    @Autowired
    private OrderService orderService;

    @Override
    @GetMapping("/popular-products")
    public String showPopularProductsPage(Model model) {
        String currentSeason = getCurrentSeason();
        model.addAttribute("currentSeason", currentSeason);

        List<ProductDTO> popularProducts = orderService.getPopularProductsForSeason(currentSeason);

        List<ProductDTO> topThreeProducts = popularProducts.size() > 3 ? popularProducts.subList(0, 3) : popularProducts;
        model.addAttribute("topThreeProducts", topThreeProducts);

        List<ProductDTO> otherProducts = popularProducts.size() > 3 ? popularProducts.subList(3, popularProducts.size()) : List.of();
        model.addAttribute("otherProducts", otherProducts);

        return "PopularProductsPage";
    }

    private String getCurrentSeason() {
        int month = LocalDateTime.now().getMonthValue();
        if (month >= 3 && month <= 5) {
            return "Spring";
        } else if (month >= 6 && month <= 8) {
            return "Summer";
        } else if (month >= 9 && month <= 11) {
            return "Autumn";
        } else {
            return "Winter";
        }
    }
}
