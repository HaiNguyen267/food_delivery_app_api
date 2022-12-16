package com.example.lesson3_food_delivery_app_api.controller;

import com.example.lesson3_food_delivery_app_api.dto.request.LoginRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.LoginResponse;
import com.example.lesson3_food_delivery_app_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        System.out.println("hihi");
        String accessToken = authService.login(loginRequest);

//        LoginResponse loginResponse = new LoginResponse("accessToken");
        LoginResponse loginResponse = new LoginResponse(accessToken);
        return ResponseEntity.ok(loginResponse);
        //
    }
}
