package com.example.clothingstore.service;

import com.example.clothingstore.dto.ProductDTO;
import java.util.List;

public interface DiscountService {
    List<ProductDTO> calculateDiscount(Long customerId, Long categoryId);
}
