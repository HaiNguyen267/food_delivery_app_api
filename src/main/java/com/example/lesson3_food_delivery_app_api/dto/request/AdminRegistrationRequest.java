package com.example.lesson3_food_delivery_app_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegistrationRequest {
    private String email;
    private String password;
}
