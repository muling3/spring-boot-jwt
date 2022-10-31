package com.example.mulinge.jwt.service;

import com.example.mulinge.jwt.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductService extends JpaRepository<Product, Long> {
    Product findByName(String name);
}
