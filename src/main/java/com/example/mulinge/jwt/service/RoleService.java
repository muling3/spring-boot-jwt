package com.example.mulinge.jwt.service;

import com.example.mulinge.jwt.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleService extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
