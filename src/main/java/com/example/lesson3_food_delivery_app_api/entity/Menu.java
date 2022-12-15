package com.example.lesson3_food_delivery_app_api.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Food> foods;

    public Long getId() {
        return id;
    }
}
