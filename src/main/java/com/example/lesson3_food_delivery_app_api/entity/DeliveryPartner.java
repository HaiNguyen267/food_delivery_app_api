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
public class DeliveryPartner extends User{
    // TODO: finish this class
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double deliveryTimeAverage;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Order> deliveredOrders;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Order> deliveringOrders;


    public double getDeliveryTimeAverage() {
        return deliveredOrders.stream()
                .map(Order::getTotalDeliveringTime)
                .mapToDouble(Long::doubleValue)
                .average()
                .orElse(0);
    }
}
