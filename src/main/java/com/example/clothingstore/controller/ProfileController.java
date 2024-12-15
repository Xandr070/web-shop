package com.example.clothingstore.controller;

import com.example.clothingstore.dto.CustomerDTO;
import com.example.clothingstore.dto.OrderDTO;
import com.example.clothingstore.dto.OrderItemDTO;
import com.example.clothingstore.entity.OrderStatus;
import com.example.clothingstore.service.impl.OrderService;
import com.example.clothingstore.service.impl.CustomerDetailsService;
import com.example.clothingstore.service.impl.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/profile")
    public String showProfilePage(@RequestParam(value = "error", required = false) String error,
                                  @RequestParam(value = "success", required = false) String success,
                                  Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomerDTO customerDTO = customerDetailsService.getCustomerByEmail(user.getUsername());
        List<OrderDTO> confirmedOrders = orderService.getOrdersByCustomerAndStatus(customerDTO.getId(), OrderStatus.CONFIRMED);
        List<OrderDTO> unconfirmedOrders = orderService.getOrdersByCustomerAndStatus(customerDTO.getId(), OrderStatus.UNCONFIRMED);
        for (OrderDTO order : confirmedOrders) {
            List<OrderItemDTO> orderItems = orderService.getOrderItemsByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }

        for (OrderDTO order : unconfirmedOrders) {
            List<OrderItemDTO> orderItems = orderService.getOrderItemsByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }
        model.addAttribute("customer", customerDTO);
        model.addAttribute("confirmedOrders", confirmedOrders);
        model.addAttribute("unconfirmedOrders", unconfirmedOrders);
        if (error != null) {
            model.addAttribute("exception", error);
        }
        if (success != null) {
            model.addAttribute("exception", success);
        }

        return "Profile";
    }
    @PostMapping("/profile/confirm-order")
    public String confirmOrder(@RequestParam("orderId") Long orderId, RedirectAttributes redirectAttributes) {

        try {
            orderService.confirmOrder(orderId);
            redirectAttributes.addAttribute("success", "Заказ успешно подтвержден.");
        } catch (IllegalStateException e) {
            redirectAttributes.addAttribute("error", "Ошибка при подтверждении заказа: " + e.getMessage());
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/add-review")
    public String addReview(@RequestParam("orderId") Long orderId,
                            @RequestParam("productId") Long productId,
                            @RequestParam("rating") int rating,
                            @RequestParam("reviewText") String reviewText,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {

        String email = principal.getName();
        Long customerId = customerDetailsService.getCustomerIdByEmail(email);

        boolean reviewExists = reviewService.checkIfReviewExists(customerId, productId);
        if (reviewExists) {
            redirectAttributes.addAttribute("error", "Вы уже оставили отзыв для этого товара.");
        } else {
            reviewService.addReview(customerId, productId, rating, reviewText);
            redirectAttributes.addAttribute("success", "Отзыв успешно добавлен.");
        }
        return "redirect:/profile";
    }

}
