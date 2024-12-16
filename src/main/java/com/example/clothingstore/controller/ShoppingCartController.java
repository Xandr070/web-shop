// ShoppingCartController.java в основном проекте
package com.example.clothingstore.controller;

import com.example.clothingstore.service.impl.OrderService;
import com.example.clothingstore.service.impl.CustomerDetailsService;
import com.example.clothingstore_contracts.controller.ShoppingCartControllerContract;
import com.example.clothingstore_contracts.input.AddToCartInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ShoppingCartController implements ShoppingCartControllerContract {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @PostMapping("/cart/add")
    @Override
    public String addToCart(AddToCartInput addToCartInput) {
        String username = getCurrentUsername();
        Long customerId = customerDetailsService.getCustomerIdByEmail(username);

        orderService.addProductToUnconfirmedOrder(customerId, addToCartInput.getProductId(), addToCartInput.getQuantity());

        return "redirect:/product?productId=" + addToCartInput.getProductId();
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
