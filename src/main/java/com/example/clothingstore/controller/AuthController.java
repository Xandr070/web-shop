package com.example.clothingstore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.clothingstore_contracts.controller.AuthControllerContract;
import com.example.clothingstore_contracts.input.CustomerInput;
import com.example.clothingstore.service.impl.CustomerDetailsService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController implements AuthControllerContract {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final CustomerDetailsService customerDetailsService;

    public AuthController(CustomerDetailsService customerDetailsService) {
        this.customerDetailsService = customerDetailsService;
    }

    @Override
    public String login() {
        logger.info("Отображение страницы входа.");
        return "login";
    }

    @Override
    @GetMapping("/register")
    public String register(Model model) {
        logger.info("Открыта страница регистрации.");
        model.addAttribute("customer", new CustomerInput());
        return "register";
    }

    @Override
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("customer") CustomerInput customerInput,
                           BindingResult bindingResult, Model model) {
        logger.info("Попытка регистрации: {}", customerInput);

        if (bindingResult.hasErrors()) {
            logger.warn("Ошибка валидации при регистрации: {}", bindingResult.getAllErrors());
            return "register";
        }

        customerDetailsService.registerCustomer(customerInput);
        logger.info("Регистрация прошла успешно для пользователя: {}", customerInput.getEmail());
        return "redirect:/login";
    }
}

