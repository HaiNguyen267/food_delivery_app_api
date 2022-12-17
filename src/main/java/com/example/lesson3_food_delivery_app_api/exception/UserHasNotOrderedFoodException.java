package com.example.lesson3_food_delivery_app_api.exception;

public class UserHasNotOrderedFoodException extends RuntimeException {
    public UserHasNotOrderedFoodException(String message) {
        super(message);
    }
}
