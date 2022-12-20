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
public class DeliveryPartner extends User{
    // TODO: finish this class
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double deliveryTimeAverage;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Order> deliveredOrders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Order> deliveringOrders = new ArrayList<>();


    public double getDeliveryTimeAverage() {
        return deliveredOrders.stream()
                .map(Order::getTotalDeliveringTime)
                .mapToDouble(Long::doubleValue)
                .average()
                .orElse(0);
    }
}
