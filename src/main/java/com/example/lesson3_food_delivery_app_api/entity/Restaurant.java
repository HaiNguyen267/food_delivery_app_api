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
public class Restaurant extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Menu menu;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurant")
    private List<FoodOrder> orders = new ArrayList<>();

}
