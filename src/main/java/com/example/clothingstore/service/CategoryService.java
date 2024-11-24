package com.example.clothingstore.service;

import com.example.clothingstore.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    Optional<CategoryDTO> getCategoryById(Long id);

    CategoryDTO saveCategory(CategoryDTO categoryDTO);

    void deleteCategory(Long categoryId);
}
