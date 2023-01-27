package com.example.lesson3_food_delivery_app_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime orderTime; // when the customer order the food
    @Column
    private LocalDateTime deliveryTime; // when the order is delivered to customer

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Transient
    private long totalDeliveringTime;

    @OneToMany(cascade = CascadeType.ALL)
    private List<FoodOrderItem> foodItems;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private DeliveryPartner deliveryPartner;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    public double getPrice() {
        return foodItems.stream()
                .mapToDouble(foodOrderItem -> foodOrderItem.getFood().getPrice() * foodOrderItem.getQuantity())
                .sum();
    }
    public String getDeliveryPartnerName() {
        if (deliveryPartner == null) return null; // if the order is not assigned to a delivery partner yet
        return deliveryPartner.getName();
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
