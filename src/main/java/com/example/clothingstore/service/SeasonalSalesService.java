package com.example.clothingstore.service;

import com.example.clothingstore.dto.ProductDTO;

import java.util.List;

public interface SeasonalSalesService {

    /**
     * Получить топ-5 товаров текущего сезона.
     *
     * @return Список ProductDTO для топ-5 товаров.
     */
    List<ProductDTO> getCurrentSeasonTopProducts();

    /**
     * Получить топ-5 товаров для указанного сезона.
     *
     * @param season Название сезона.
     * @return Список ProductDTO для топ-5 товаров.
     */
    List<ProductDTO> getTopProductsForSeason(String season);
}
