package com.example.lesson3_food_delivery_app_api.repository;

import com.example.lesson3_food_delivery_app_api.entity.Restaurant;
import com.example.lesson3_food_delivery_app_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String restaurantEmail);

    List<Restaurant> findByNameContainingIgnoreCase(String name);
}
