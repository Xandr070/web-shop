package com.example.clothingstore.service.impl;

import com.example.clothingstore.dto.OrderItemDTO;
import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.entity.OrderItem;
import com.example.clothingstore.entity.Product;
import com.example.clothingstore.repository.OrderItemRepository;
import com.example.clothingstore.service.SeasonalSalesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeasonalSalesServiceImpl implements SeasonalSalesService {

    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SeasonalSalesServiceImpl(OrderItemRepository orderItemRepository, ModelMapper modelMapper) {
        this.orderItemRepository = orderItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductDTO> getCurrentSeasonTopProducts() {
        String currentSeason = determineCurrentSeason();
        return getTopProductsForSeason(currentSeason);
    }

    @Override
    public List<ProductDTO> getTopProductsForSeason(String season) {
        List<Month> seasonMonths = getSeasonMonths(season);

        if (seasonMonths == null) {
            throw new IllegalArgumentException("Недопустимый сезон: " + season);
        }

        Map<Product, Long> productSales = orderItemRepository.findAllByOrderDateInMonths(seasonMonths)
                .stream()
                .collect(Collectors.groupingBy(
                        OrderItem::getProduct,
                        Collectors.summingLong(OrderItem::getQuantity)
                ));

        return productSales.entrySet().stream()
                .sorted(Map.Entry.<Product, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> modelMapper.map(entry.getKey(), ProductDTO.class)) // Использование ModelMapper
                .collect(Collectors.toList());
    }

    private String determineCurrentSeason() {
        Month currentMonth = Month.from(new Date().toInstant());
        if (currentMonth == Month.JUNE || currentMonth == Month.JULY || currentMonth == Month.AUGUST) {
            return "лето";
        } else if (currentMonth == Month.SEPTEMBER || currentMonth == Month.OCTOBER || currentMonth == Month.NOVEMBER) {
            return "осень";
        } else if (currentMonth == Month.DECEMBER || currentMonth == Month.JANUARY || currentMonth == Month.FEBRUARY) {
            return "зима";
        } else {
            return "весна";
        }
    }

    private List<Month> getSeasonMonths(String season) {
        return switch (season.toLowerCase()) {
            case "лето" -> List.of(Month.JUNE, Month.JULY, Month.AUGUST);
            case "осень" -> List.of(Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER);
            case "зима" -> List.of(Month.DECEMBER, Month.JANUARY, Month.FEBRUARY);
            case "весна" -> List.of(Month.MARCH, Month.APRIL, Month.MAY);
            default -> null;
        };
    }

    private OrderItemDTO convertToDTO(OrderItem orderItem) {
        return modelMapper.map(orderItem, OrderItemDTO.class);
    }
}
