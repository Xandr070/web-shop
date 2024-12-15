package com.example.clothingstore.controller;

import com.example.clothingstore.dto.CategoryDTO;
import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.service.impl.CategoryService;
import com.example.clothingstore.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/add-product")
    public String showAddProductPage(Model model) {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "add-product";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/add-product")
    public String addProduct(@RequestParam String name,
                             @RequestParam Double price,
                             @RequestParam Integer stock,
                             @RequestParam Long categoryId) {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setPrice(price);
        productDTO.setStock(stock);
        productDTO.setCategoryId(categoryId);

        productService.addProduct(productDTO);

        return "redirect:/store";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/edit-product/{id}")
    public String showEditProductPage(@PathVariable Long id, Model model) {
        ProductDTO productDTO = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<CategoryDTO> categories = categoryService.getAllCategories();

        // Добавляем в модель
        model.addAttribute("product", productDTO);
        model.addAttribute("categories", categories);

        return "edit-product";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/edit-product/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute ProductDTO updatedProductDTO) {
        productService.updateProduct(id, updatedProductDTO);

        return "redirect:/store";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return "redirect:/store";
    }
}

