package com.example.lesson3_food_delivery_app_api.dto.request;

import com.example.lesson3_food_delivery_app_api.entity.Food;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFoodRequest {

    @Data
    public static class FoodInfo {
        private Long foodId;
        private Integer quantity;
    }

    private List<FoodInfo> order;
    public Integer getQuantityOfFood(Long foodId) {
        return order.stream()
                .filter(foodInfo -> foodInfo.getFoodId().equals(foodId))
                .findFirst()
                .map(FoodInfo::getQuantity)
                .orElse(0);
    }

}
