package com.example.lesson3_food_delivery_app_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {
    private int status;
    private String message;
    private Object data;

    public SuccessResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}
