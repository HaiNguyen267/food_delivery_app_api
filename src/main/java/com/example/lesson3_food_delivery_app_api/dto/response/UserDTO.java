package com.example.lesson3_food_delivery_app_api.dto.response;

import com.example.lesson3_food_delivery_app_api.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class UserDTO {
    private Long id;

    private String email;

    private String role;

    private boolean isLocked;

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole().toString();
        this.isLocked = user.isLocked();
    }
}
