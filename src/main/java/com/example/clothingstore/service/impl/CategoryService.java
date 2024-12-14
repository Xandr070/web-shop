package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.CategoryDTO;
import com.example.clothingstore.repository.CategoryRepository;
import com.example.clothingstore.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    CategoryDTO dto = new CategoryDTO();
                    dto.setId(category.getId());
                    dto.setName(category.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
