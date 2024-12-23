package com.example.clothingstore.dto;

public class ReviewDTO {
    private Long id;
    private String productName;
    private String customerName;
    private Integer rating;
    private String reviewText;
    private String categoryName;

    public ReviewDTO() {
    }

    public ReviewDTO(Long id, String productName, String customerName, Integer rating, String reviewText, String categoryName) {
        this.id = id;
        this.productName = productName;
        this.customerName = customerName;
        this.rating = rating;
        this.reviewText = reviewText;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
