package com.example.clothingstore.controller;

import com.example.clothingstore_contracts.controller.SeasonalSalesController;
import com.example.clothingstore_contracts.input.SeasonInputModel;
import com.example.clothingstore_contracts.viewmodel.TopProductViewModel;
import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore.service.SeasonalSalesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SeasonalSalesControllerImpl implements SeasonalSalesController {

    private final SeasonalSalesService seasonalSalesService;
    private final ModelMapper modelMapper;

    @Autowired
    public SeasonalSalesControllerImpl(SeasonalSalesService seasonalSalesService, ModelMapper modelMapper) {
        this.seasonalSalesService = seasonalSalesService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<List<TopProductViewModel>> getSeasonalSales() {
        List<ProductDTO> topProductsDTO = seasonalSalesService.getCurrentSeasonTopProducts();

        List<TopProductViewModel> topProductsViewModel = topProductsDTO.stream()
                .map(this::convertToTopProductViewModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(topProductsViewModel);
    }

    @Override
    public ResponseEntity<List<TopProductViewModel>> getTopProductsBySeason(SeasonInputModel input) {
        List<ProductDTO> topProductsDTO = seasonalSalesService.getTopProductsForSeason(input.getSeason());

        List<TopProductViewModel> topProductsViewModel = topProductsDTO.stream()
                .map(this::convertToTopProductViewModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(topProductsViewModel);
    }

    private TopProductViewModel convertToTopProductViewModel(ProductDTO productDTO) {
        return modelMapper.map(productDTO, TopProductViewModel.class);
    }
}
