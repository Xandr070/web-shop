package com.example.clothingstore.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @PostMapping("/cart/add")
    @Override
    public String addToCart(AddToCartInput addToCartInput) {
        String username = getCurrentUsername();
        logger.info("Пользователь '{}' добавляет продукт '{}' в корзину (количество: {}).", username, addToCartInput.getProductId(), addToCartInput.getQuantity());

        Long customerId = customerDetailsService.getCustomerIdByEmail(username);
        orderService.addProductToUnconfirmedOrder(customerId, addToCartInput.getProductId(), addToCartInput.getQuantity());

        logger.info("Продукт '{}' успешно добавлен в корзину пользователя '{}'.", addToCartInput.getProductId(), username);
        return "redirect:/product?productId=" + addToCartInput.getProductId();
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            return userDetails.getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        logger.error("Не удалось определить текущего пользователя.");
        throw new RuntimeException("Не удалось определить пользователя.");
    }
}

