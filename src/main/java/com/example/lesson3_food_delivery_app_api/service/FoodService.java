package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.entity.Food;
import com.example.lesson3_food_delivery_app_api.repository.FoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public void saveFood(Food food) {
        foodRepository.save(food);

    }

    public void deleteFoodById(long foodId) {
        foodRepository.deleteById(foodId);
    }

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public Food getFoodById(Long foodId) {
        // TODO: use NotFoundException to use ControllerAdvice
        return foodRepository.findById(foodId).orElseThrow(() -> new RuntimeException("Food not found"));
    }
}
