package com.example.mulinge.jwt.utils;

import com.example.mulinge.jwt.entities.Product;

public class AddProductToUser {
    private String username;
    private Product product;

    public AddProductToUser(String username, Product product) {
        this.username = username;
        this.product = product;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}