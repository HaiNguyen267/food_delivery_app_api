package com.example.lesson3_food_delivery_app_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Order {

    public static enum OrderStatus {
         READY, DELIVERING, DELIVERED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: admin can view an order
    private int quantity;
    private double price;
    private LocalDateTime orderTime; // when the customer order the food
    private LocalDateTime deliveryTime; // when the order is delivered to customer

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Transient
    private long totalDeliveringTime;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private Food food;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private DeliveryPartner deliveryPartner;


    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;


    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private Customer customer;

    public Long getFoodId() {
        return food.getId();
    }

    public Long getDeliveryPartnerId() {
        if (deliveryPartner == null) return null;
        return deliveryPartner.getId();
    }

    public Long getRestaurantId() {
        return restaurant.getId();
    }
    public double getPrice() {
        return food.getPrice() * quantity;
    }

    public String getOrderTime() {
       return orderTime.toString();
    }

    public String getDeliveryTime() {
        return deliveryTime.toString();
    }

    public long getTotalDeliveringTime() {
        // calculate the total minutes from orderTime to deliveryTime
        long minutes = ChronoUnit.MINUTES.between(orderTime, deliveryTime);
        return minutes;
    }

}
