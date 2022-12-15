package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.request.RestaurantRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.RegisterResponse;
import com.example.lesson3_food_delivery_app_api.entity.Menu;
import com.example.lesson3_food_delivery_app_api.entity.Restaurant;
import com.example.lesson3_food_delivery_app_api.jwt.JwtProvider;
import com.example.lesson3_food_delivery_app_api.repository.RestaurantRepository;
import com.example.lesson3_food_delivery_app_api.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<?> register(RestaurantRegistrationRequest registrationRequest) {
        String email = registrationRequest.getEmail();
        String password = registrationRequest.getPassword();
        String address = registrationRequest.getAddress();
        String phone = registrationRequest.getPhone();
        String name = registrationRequest.getName();

        if (restaurantRepository.existsByEmailIgnoreCase(email)) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        Restaurant restaurant = Restaurant.builder()
                .name(name)
                .address(address)
                .phone(phone)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        restaurant.setRole(Role.RESTAURANT);
        restaurant = restaurantRepository.save(restaurant);

        String accessToken = JwtProvider.generateToken(restaurant);
        RegisterResponse loginResponse = new RegisterResponse(accessToken);
        return ResponseEntity.ok().body(loginResponse);
    }

    public List<Menu> addMenu(String restaurantEmail, Menu menu) {
        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);
        restaurant.setMenu(menu);
        return null;
    }

    private Restaurant getRestaurantByEmail(String restaurantEmail) {
        return restaurantRepository.findByEmail(restaurantEmail)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }
}
