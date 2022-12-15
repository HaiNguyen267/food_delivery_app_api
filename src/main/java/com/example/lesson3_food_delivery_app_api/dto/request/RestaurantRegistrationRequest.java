package com.example.lesson3_food_delivery_app_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRegistrationRequest {

    private String name;
    private String address;
    private String phone;
    private String email;
    private String password;
}
