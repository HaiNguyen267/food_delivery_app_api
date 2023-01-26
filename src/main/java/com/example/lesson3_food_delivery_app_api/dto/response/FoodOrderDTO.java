package com.example.lesson3_food_delivery_app_api.dto.response;

import com.example.lesson3_food_delivery_app_api.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodOrderDTO {
    private Long foodId;
    private String restaurantName;
    private Long orderId;
    private String foodName;
    private Long deliveryPartnerId;
    private int quantity;
    private double price;

    public FoodOrderDTO(Order order) {
        this.orderId = order.getId();
        this.price = order.getPrice();
        this.foodId = order.getFood().getId();
        // if the order is being delivering by a delivery partner, then we return the delivery partner id
        if (order.getDeliveryPartner() != null) {
            this.deliveryPartnerId = order.getDeliveryPartner().getId();
        }
    }

}
