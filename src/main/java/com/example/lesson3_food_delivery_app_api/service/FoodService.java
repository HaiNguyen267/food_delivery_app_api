package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.entity.Food;
import com.example.lesson3_food_delivery_app_api.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodService {
    @Autowired
    private FoodRepository foodRepository;

    public void saveFood(Food food) {
        foodRepository.save(food);

    }

    public void deleteFoodById(Long foodId) {
        foodRepository.deleteById(foodId);
    }
}
