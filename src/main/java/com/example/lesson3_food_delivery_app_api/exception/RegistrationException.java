package com.example.lesson3_food_delivery_app_api.exception;

//TODO: catch this exception in ControllerAdvice
public class RegistrationException extends RuntimeException {
    public RegistrationException(String message) {
        super(message);
    }
}
