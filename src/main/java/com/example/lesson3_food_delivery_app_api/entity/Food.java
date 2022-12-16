package com.example.lesson3_food_delivery_app_api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
