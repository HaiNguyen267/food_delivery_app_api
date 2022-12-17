package com.example.lesson3_food_delivery_app_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: admin can view an order
    private int quantity;
    private double price;
    private Date orderTime;
    private Date deliveryTime;
    private boolean isDelivered;

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
        return parseDate(orderTime);
    }

    public String getDeliveryTime() {
        return parseDate(deliveryTime);
    }

    public static String parseDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);
    }
}
