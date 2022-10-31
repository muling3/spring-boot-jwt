package com.example.mulinge.jwt.entities;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "roles_tbl")
public class Role {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    public Role() {
    }

    public Role(String name){
        this.name = name;
    }

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
