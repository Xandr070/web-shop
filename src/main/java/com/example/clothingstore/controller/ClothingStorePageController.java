package com.example.clothingstore.controller;

import com.example.clothingstore.dto.CategoryDTO;
import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.service.impl.CategoryService;
import com.example.clothingstore.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ClothingStorePageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/store")
    public String showStorePage(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Model model) {

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
}
