package com.example.lesson3_food_delivery_app_api.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FoodCommentRequest {
    private Long foodId;
    private String comment;
}
