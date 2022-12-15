package com.example.lesson3_food_delivery_app_api.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price;
    private String description;
    private String imageUrl;

    @OneToOne(cascade = CascadeType.ALL)
    private Rating rating;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;
}
