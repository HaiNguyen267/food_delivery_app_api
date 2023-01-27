package com.example.lesson3_food_delivery_app_api.dto.response;

import com.example.lesson3_food_delivery_app_api.entity.FoodOrder;
import com.example.lesson3_food_delivery_app_api.entity.FoodOrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodOrderDTO {

    @Data
    @NoArgsConstructor
    public static class FoodItemDTO {
        private Long foodId;
        private String foodName;
        private double price;
        private int quantity;

        public FoodItemDTO(FoodOrderItem foodOrderItem) {
            this.foodId = foodOrderItem.getFood().getId();
            this.foodName = foodOrderItem.getFood().getName();
            this.price = foodOrderItem.getFood().getPrice();
            this.quantity = foodOrderItem.getQuantity();
        }
    }

    private Long orderId;
    private List<FoodItemDTO> foodItems;
    private Long restaurantId;
    private String restaurantName;
    private Long deliveryPartnerId;
    private double totalPrice;


    public FoodOrderDTO(FoodOrder foodOrder) {
        this.orderId = foodOrder.getId();
        this.foodItems = foodOrder.getFoodItems().stream().map(FoodItemDTO::new).toList();
        this.restaurantId = foodOrder.getRestaurant().getId();
        this.restaurantName = foodOrder.getRestaurant().getName();
        this.deliveryPartnerId = foodOrder.getDeliveryPartner() == null ? null : foodOrder.getDeliveryPartner().getId();
        this.totalPrice = foodOrder.getPrice();
    }



}
