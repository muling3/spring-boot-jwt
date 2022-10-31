package com.example.mulinge.jwt.service;

import com.example.mulinge.jwt.entities.Product;
import com.example.mulinge.jwt.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserService extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);
    UserModel findByEmail(String email);
}
