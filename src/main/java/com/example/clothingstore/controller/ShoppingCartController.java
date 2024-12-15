package com.example.clothingstore.controller;

import com.example.clothingstore.service.impl.OrderService;
import com.example.clothingstore.service.impl.CustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShoppingCartController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, Model model) {
        String username = getCurrentUsername();
        Long customerId = customerDetailsService.getCustomerIdByEmail(username);

        orderService.addProductToUnconfirmedOrder(customerId, productId, quantity);

        return "redirect:/product?productId=" + productId;
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            return userDetails.getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        throw new RuntimeException("Не удалось определить пользователя.");
    }
}
