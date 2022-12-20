package com.example.lesson3_food_delivery_app_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFoodResponse {
    private Long foodId;
    private String restaurantName;
    private Long orderId;
    private String foodName;
    private int quantity;
    private double price;
}
