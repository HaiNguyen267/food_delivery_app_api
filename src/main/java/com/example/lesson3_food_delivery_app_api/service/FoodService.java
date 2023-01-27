package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.entity.Food;
import com.example.lesson3_food_delivery_app_api.exception.NotFoundException;
import com.example.lesson3_food_delivery_app_api.repository.FoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public Food saveFood(Food food) {
        return foodRepository.save(food);
    }

    public void deleteFoodById(long foodId) {
        foodRepository.deleteById(foodId);
    }

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public Food getFoodById(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> new NotFoundException("Food not found for id: " + foodId));
    }

    public List<Food> getFoodsByNameContaining(String name) {
        return foodRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Food> getFoodsByIds(List<Long> foodIds) {
        return foodIds.isEmpty() ? List.of()
                : foodIds.stream()
                .map(this::getFoodById)
                .toList();
    }
}
