package com.example.lesson3_food_delivery_app_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeAccessRequest {
    public enum Operation{
        LOCK, UNLOCK
    }

    private long userId;
    private Operation operation;
}
