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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Table(name = "orders") // cannot name it 'order' because it is a reserved word in SQL
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Order{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: admin can view an order
    private int quantity;

    @Transient
    private double price;

    @Column
    private LocalDateTime orderTime; // when the customer order the food
    @Column
    private LocalDateTime deliveryTime; // when the order is delivered to customer

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Transient
    private long totalDeliveringTime;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private Food food;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private DeliveryPartner deliveryPartner;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;



    public String getFoodName() {
        return food.getName();
    }

    public String getDeliveryPartnerName() {
        if (deliveryPartner == null) return null; // if the order is not assigned to a delivery partner yet
        return deliveryPartner.getName();
    }

    public String getRestaurantName() {
        return restaurant.getName();
    }

    public double getPrice() {
        return food.getPrice() * quantity;
    }

    public String getOrderTime() {
       return orderTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getDeliveryTime() {
        if (deliveryTime == null) return null;
        return deliveryTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @JsonIgnore
    public long getTotalDeliveringTime() {
        // calculate the total minutes from orderTime to deliveryTime
        long minutes = ChronoUnit.MINUTES.between(orderTime, deliveryTime);
        return minutes;
    }


}
