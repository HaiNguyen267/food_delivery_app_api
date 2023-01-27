package com.example.lesson3_food_delivery_app_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "food")
    private List<Rating> ratings = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "food")
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    private Menu menu;


    public Long getRestaurantId() {
        return menu.getRestaurant().getId();
    }

    public String getRestaurantName() {
        return menu.getRestaurant().getName();
    }

    public Double getRating() {
        // when there is no rating, then  return null
        if (ratings.size() == 0) {
            return null;
        }
        return ratings.stream()
                .map(Rating::getRating)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    public int getNumberOfComments() {
        return comments.size();
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
