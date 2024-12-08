package com.example.clothingstore.dto;

public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
    private String categoryName;

    public ProductDTO(Long id, String name, Double price, Integer stock, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categoryName = categoryName;
    }

    public ProductDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
