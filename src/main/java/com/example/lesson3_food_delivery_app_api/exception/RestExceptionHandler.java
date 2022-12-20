package com.example.lesson3_food_delivery_app_api.exception;

import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({
            UserLockedException.class,
            OrderDeliveringException.class,
            RatingInvalidException.class,
            UserHasNotOrderedFoodException.class,
            WrongUsernamePasswordException.class,
            InvalidJWTTokenException.class
    })
    public ResponseEntity<?> handleException(Exception e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
