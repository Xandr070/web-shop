package com.example.clothingstore.controller;

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

    private final CustomerDetailsService customerDetailsService;

    public AuthController(CustomerDetailsService customerDetailsService) {
        this.customerDetailsService = customerDetailsService;
    }

    @Override
    public String login() {
        return "login";
    }

    @Override
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("customer", new CustomerInput());
        return "register";
    }

    @Override
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("customer") CustomerInput customerInput,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        customerDetailsService.registerCustomer(customerInput);

        return "redirect:/login";
    }
}
