package com.example.clothingstore.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.service.impl.CategoryService;
import com.example.clothingstore.service.impl.ProductService;
import com.example.clothingstore_contracts.controller.ProductControllerContract;
import com.example.clothingstore_contracts.viewmodel.ProductViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ProductController implements ProductControllerContract {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Override
    public String addProduct(@RequestParam String name,
                             @RequestParam Double price,
                             @RequestParam Integer stock,
                             @RequestParam Long categoryId) {
        logger.info("Добавление продукта: name={}, price={}, stock={}, categoryId={}", name, price, stock, categoryId);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setPrice(price);
        productDTO.setStock(stock);
        productDTO.setCategoryId(categoryId);

        productService.addProduct(productDTO);

        logger.info("Продукт успешно добавлен: {}", productDTO);
        return "redirect:/store";
    }

    @Override
    public String editProduct(@PathVariable Long id, @ModelAttribute ProductViewModel productViewModel) {
        logger.info("Редактирование продукта с ID={}: {}", id, productViewModel);

        ProductDTO updatedProductDTO = new ProductDTO();
        updatedProductDTO.setId(id);
        updatedProductDTO.setName(productViewModel.getName());
        updatedProductDTO.setPrice(productViewModel.getPrice());
        updatedProductDTO.setStock(productViewModel.getStock());
        updatedProductDTO.setCategoryId(productViewModel.getCategoryId());

        productService.updateProduct(id, updatedProductDTO);

        logger.info("Продукт успешно обновлен: {}", updatedProductDTO);
        return "redirect:/store";
    }

    @Override
    public String showAddProductPage(Model model) {
        logger.info("Открыта страница добавления продукта.");
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add-product";
    }

    @Override
    public String showEditProductPage(@PathVariable Long id, Model model) {
        logger.info("Открыта страница редактирования продукта с ID={}", id);

        ProductDTO productDTO = productService.getProductById(id)
                .orElseThrow(() -> {
                    logger.error("Продукт с ID={} не найден.", id);
                    return new RuntimeException("Product not found");
                });

        model.addAttribute("product", productDTO);
        return "edit-product";
    }

    @Override
    public String deleteProduct(@PathVariable Long id) {
        logger.info("Удаление продукта с ID={}", id);
        productService.deleteProduct(id);
        logger.info("Продукт с ID={} успешно удален.", id);
        return "redirect:/store";
    }
}
