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
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            mappedBy = "menu",
            fetch = FetchType.LAZY
    )
    private List<Food> foods = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "menu")
    private Restaurant restaurant;
}
