package com.example.mulinge.jwt.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.mulinge.jwt.entities.Product;
import com.example.mulinge.jwt.entities.Role;
import com.example.mulinge.jwt.entities.UserModel;
import com.example.mulinge.jwt.serviceimpl.ServicesImplementation;
import com.example.mulinge.jwt.utils.AddProductToUser;
import com.example.mulinge.jwt.utils.AddUserRoleForm;
import com.example.mulinge.jwt.utils.JWTConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class ApplicationController {

    @Autowired
    ServicesImplementation serviceImpl;

    @PostMapping("/user")
    ResponseEntity<UserModel> createUser(@RequestBody UserModel user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user").toUriString());
        UserModel u = serviceImpl.createUser(user);
        return ResponseEntity.created(uri).body(u);
    }

    @PostMapping("/product")
    ResponseEntity<Product> createUser(@RequestBody Product product) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product").toUriString());
        Product u = serviceImpl.createProduct(product);
        return ResponseEntity.created(uri).body(u);
    }

    @PostMapping("/role")
    ResponseEntity<Role> createRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product").toUriString());
        Role u = serviceImpl.createRole(role);
        return ResponseEntity.created(uri).body(u);
    }

    @PostMapping("/products/add")
    ResponseEntity<?> addProductToUser(@RequestBody AddProductToUser addProductToUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/" + addProductToUser.getUsername() + "/" + addProductToUser.getProduct().getName()).toUriString());
        serviceImpl.addProductToUser(addProductToUser.getUsername(), addProductToUser.getProduct());
        return ResponseEntity.created(uri).body(null);
    }

    @PostMapping("/roles/add")
    ResponseEntity<?> addRoleToUser(@RequestBody AddUserRoleForm addUserRoleForm) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/" + addUserRoleForm.getUsername() + "/" + addUserRoleForm.getRoleName()).toUriString());
        serviceImpl.addRoleToUser(addUserRoleForm.getUsername(), addUserRoleForm.getRoleName());
        return ResponseEntity.created(uri).body(null);
    }


    @GetMapping("/users")
    ResponseEntity<List<UserModel>> getUsers() {
        List<UserModel> users = serviceImpl.getUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/products")
    ResponseEntity<List<Product>> getProducts() {
        List<Product> products = serviceImpl.getProducts();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/product")
    ResponseEntity<Product> getProduct(@RequestParam String name) {
        Product product = serviceImpl.getProduct(name);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/products/{user}")
    ResponseEntity<List<Product>> getProductsByUser(@PathVariable String user) {
        List<Product> products = serviceImpl.getProductsByUser(user);
        return ResponseEntity.ok().body(products);
    }

    //refresh route handler
    @GetMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //getting authorization header
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        //check if it starts with bearer
        if (authorization != null && authorization.startsWith("Bearer")) {
            //get the token
            try {

                String token = authorization.split(" ")[1];

                Map<String, String> newTokens = serviceImpl.refreshRoute(token);

                new ObjectMapper().writeValue(response.getOutputStream(), newTokens);

            } catch (Exception exception) {
                Map<String, String> errResponse = new HashMap<>();
                errResponse.put("error", exception.getMessage());

                response.setStatus(HttpStatus.FORBIDDEN.value());
                new ObjectMapper().writeValue(response.getOutputStream(), errResponse);
            }
        } else {
            throw new Exception("No access token");
        }
    }

}
