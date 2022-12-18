package com.example.lesson3_food_delivery_app_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    // TODO: customer can see comments of a food
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price;
    private String description;
    private String imageUrl;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Rating> ratings;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    public Long getRestaurantId() {
        return restaurant.getId();
    }

    public double getRating() {
        return ratings.stream()
                .map(Rating::getRating)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    public int numberOfComment() {
        return comments.size();
    }
}
