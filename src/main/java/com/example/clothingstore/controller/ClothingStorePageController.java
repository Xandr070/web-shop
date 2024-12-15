package com.example.clothingstore.controller;

import com.example.clothingstore.dto.CategoryDTO;
import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.dto.OrderDTO;
import com.example.clothingstore.service.impl.CategoryService;
import com.example.clothingstore.service.impl.CustomerDetailsService;
import com.example.clothingstore.service.impl.OrderService;
import com.example.clothingstore.service.impl.ProductService;
import com.example.clothingstore.entity.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ClothingStorePageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/store")
    public String showStorePage(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            username = userDetails.getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new RuntimeException("Не удалось определить пользователя.");
        }

        Long customerId = customerDetailsService.getCustomerIdByEmail(username);

        model.addAttribute("CustomerId", customerId);

        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        List<ProductDTO> products = getFilteredProducts(categoryId, minPrice, maxPrice);
        model.addAttribute("products", products);

        return "Main";
    }

    private List<ProductDTO> getFilteredProducts(Long categoryId, Double minPrice, Double maxPrice) {
        if (categoryId != null) {
            return productService.getProductsByCategory(categoryId);
        } else if (minPrice != null && maxPrice != null) {
            return productService.filterProductsByPrice(minPrice, maxPrice);
        } else {
            return productService.getAllProducts();
        }
    }

    @GetMapping("/product")
    public String showProductPage(@RequestParam Long productId, Model model) {
        ProductDTO product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        List<ProductDTO> relatedProducts = productService.getProductsByCategory(product.getCategoryId());
        relatedProducts.removeIf(p -> p.getId().equals(productId));
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);

        return "ProductDetails";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, Model model) {
        String username = getCurrentUsername();
        Long customerId = customerDetailsService.getCustomerIdByEmail(username);

        orderService.addProductToUnconfirmedOrder(customerId, productId, quantity);

        return "redirect:/store";
    }

    @GetMapping("/profile/orders")
    public String showOrders(Model model) {
        String username = getCurrentUsername();
        Long customerId = customerDetailsService.getCustomerIdByEmail(username);

        List<OrderDTO> unconfirmedOrders = orderService.getOrdersByCustomerAndStatus(customerId, OrderStatus.UNCONFIRMED);
        List<OrderDTO> confirmedOrders = orderService.getOrdersByCustomerAndStatus(customerId, OrderStatus.CONFIRMED);

        model.addAttribute("unconfirmedOrders", unconfirmedOrders);
        model.addAttribute("confirmedOrders", confirmedOrders);

        return "ProfileOrders";
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
