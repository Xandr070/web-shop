package com.example.clothingstore.controller;

import com.example.clothingstore.dto.ProductDTO;
import com.example.clothingstore_contracts.controller.DiscountController;
import com.example.clothingstore_contracts.input.DiscountInputModel;
import com.example.clothingstore_contracts.viewmodel.CustomerReviewDiscountViewModel;
import com.example.clothingstore.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DiscountControllerImpl implements DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountControllerImpl(DiscountService discountService) {
        this.discountService = discountService;
    }

    @Override
    public ResponseEntity<CustomerReviewDiscountViewModel> getDiscountForCustomer(DiscountInputModel input) {

        List<ProductDTO> discountedProducts = discountService.calculateDiscount(input.getCustomerId(), input.getCategoryId());

        if (!discountedProducts.isEmpty()) {
            ProductDTO product = discountedProducts.get(0);
            CustomerReviewDiscountViewModel viewModel = new CustomerReviewDiscountViewModel();
            viewModel.setCustomerName("Customer " + input.getCustomerId());
            viewModel.setCategoryName(product.getCategoryName());
            viewModel.setReviewCount(discountedProducts.size());
            viewModel.setDiscountPercentage(10.0);
            return ResponseEntity.ok(viewModel);
        } else {
            return ResponseEntity.ok(new CustomerReviewDiscountViewModel());
        }
    }
}
