package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.entity.Product;
import com.example.clothingstore.repository.OrderItemRepository;
import com.example.clothingstore.repository.ProductRepository;
import com.example.clothingstore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, OrderItemRepository orderItemRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> modelMapper.map(product, ProductDTO.class));
    }

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<ProductDTO> getTopProductsBySalesSeason(String season) {
        List<Integer> seasonMonths = getSeasonMonths(season);

        List<Object[]> topProductsData = orderItemRepository.findTopProductsBySeason(seasonMonths);

        return topProductsData.stream()
                .map(data -> modelMapper.map(data[0], ProductDTO.class)) // Преобразуем каждый объект в DTO
                .collect(Collectors.toList());
    }

    private List<Integer> getSeasonMonths(String season) {
        switch (season.toLowerCase()) {
            case "зима": return List.of(12, 1, 2);
            case "весна": return List.of(3, 4, 5);
            case "лето": return List.of(6, 7, 8);
            case "осень": return List.of(9, 10, 11);
            default: throw new IllegalArgumentException("Неверный сезон: " + season);
        }
    }
}
