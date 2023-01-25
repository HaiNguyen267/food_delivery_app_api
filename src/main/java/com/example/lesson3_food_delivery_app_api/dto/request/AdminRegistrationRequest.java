package com.example.lesson3_food_delivery_app_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegistrationRequest {
    @Schema(description = "Admin's first name", example = "John")
    private String email;
    private String password;
}
