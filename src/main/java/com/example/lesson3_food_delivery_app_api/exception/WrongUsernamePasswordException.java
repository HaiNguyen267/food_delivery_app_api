package com.example.lesson3_food_delivery_app_api.exception;

public class WrongUsernamePasswordException extends RuntimeException {
    public WrongUsernamePasswordException(String message) {
        super(message);
    }
}
